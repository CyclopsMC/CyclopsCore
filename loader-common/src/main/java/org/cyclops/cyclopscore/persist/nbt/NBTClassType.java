package org.cyclops.cyclopscore.persist.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.datastructure.EnumFacingMap;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;
import org.cyclops.cyclopscore.helper.IModHelpers;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Types of NBT field classes used for persistence of fields in {@link CyclopsBlockEntity}.
 * @author rubensworks
 *
 * @param <T> The field class type.
 * @see NBTPersist
 */
public abstract class NBTClassType<T> {

    /**
     * A map of all the types to their persist actions.
     */
    public static Map<Class<?>, NBTClassType<?>> NBTYPES = new IdentityHashMap<>();
    static {
        NBTYPES.put(Integer.class, new NBTClassType<Integer>() {

            @Override
            public void writePersistedField(String name, Integer object, ValueOutput tag) {
                tag.putInt(name, object);
            }

            @Override
            public Integer readPersistedField(String name, ValueInput tag) {
                return tag.getInt(name).orElseThrow();
            }

            @Override
            public Integer getDefaultValue() {
                return 0;
            }
        });
        NBTYPES.put(int.class, NBTYPES.get(Integer.class));

        NBTYPES.put(Float.class, new NBTClassType<Float>() {

            @Override
            public void writePersistedField(String name, Float object, ValueOutput tag) {
                tag.putFloat(name, object);
            }

            @Override
            public Float readPersistedField(String name, ValueInput tag) {
                return tag.getFloatOr(name, 0);
            }

            @Override
            public Float getDefaultValue() {
                return 0F;
            }
        });
        NBTYPES.put(float.class, NBTYPES.get(Float.class));

        NBTYPES.put(Boolean.class, new NBTClassType<Boolean>() {

            @Override
            public void writePersistedField(String name, Boolean object, ValueOutput tag) {
                tag.putBoolean(name, object);
            }

            @Override
            public Boolean readPersistedField(String name, ValueInput tag) {
                return tag.getBooleanOr(name, false);
            }

            @Override
            public Boolean getDefaultValue() {
                return false;
            }
        });
        NBTYPES.put(boolean.class, NBTYPES.get(Boolean.class));

        NBTYPES.put(String.class, new NBTClassType<String>() {

            @Override
            public void writePersistedField(String name, String object, ValueOutput tag) {
                if(object != null && !object.isEmpty()) {
                    tag.putString(name, object);
                }
            }

            @Override
            public String readPersistedField(String name, ValueInput tag) {
                return tag.getString(name).orElseThrow();
            }

            @Override
            public String getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Direction.class, new NBTClassType<Direction>() {
            @Override
            public void writePersistedField(String name, Direction object, ValueOutput tag) {
                tag.putInt(name, object.ordinal());
            }

            @Override
            public Direction readPersistedField(String name, ValueInput tag) {
                return Direction.values()[tag.getInt(name).orElseThrow()];
            }

            @Override
            public Direction getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Fluid.class, new NBTClassType<Fluid>() {
            @Override
            public void writePersistedField(String name, Fluid object, ValueOutput tag) {
                tag.putString(name, BuiltInRegistries.FLUID.getKey(object).toString());
            }

            @Override
            public Fluid readPersistedField(String name, ValueInput tag) {
                String fluidName = tag.getString(name).orElseThrow();
                return BuiltInRegistries.FLUID.getValue(Identifier.parse(fluidName));
            }

            @Override
            public Fluid getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Tag.class, new NBTClassType<Tag>() {

            @Override
            public void writePersistedField(String name, Tag object, ValueOutput tag) {
                tag.store(name, ExtraCodecs.NBT, object);
            }

            @Override
            public Tag readPersistedField(String name, ValueInput tag) {
                return tag.read(name, ExtraCodecs.NBT).orElseThrow();
            }

            @Override
            public Tag getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Set.class, new CollectionNBTClassType<Set>() {

            @Override
            protected Set createNewCollection() {
                return Sets.newHashSet();
            }
        });

        NBTYPES.put(List.class, new CollectionNBTClassType<List>() {

            @Override
            protected List createNewCollection() {
                return Lists.newLinkedList();
            }
        });

        NBTYPES.put(Map.class, new NBTClassType<Map>() {

            @SuppressWarnings("unchecked")
            @Override
            public void writePersistedField(String name, Map object, ValueOutput tag) {
                ValueOutput mapTag = tag.child(name);
                ValueOutput.ValueOutputList list = mapTag.childrenList("map");
                boolean setKeyType = false;
                boolean setValueType = false;
                for(Map.Entry entry : (Set<Map.Entry>) object.entrySet()) {
                    ValueOutput entryTag = list.addChild();
                    getType(entry.getKey().getClass(), object).writePersistedField("k", entry.getKey(), entryTag);
                    if(entry.getValue() != null) {
                        getType(entry.getValue().getClass(), object).writePersistedField("v", entry.getValue(), entryTag.child("v"));
                    }

                    if(!setKeyType) {
                        setKeyType = true;
                        mapTag.putString("keyType", entry.getKey().getClass().getName());
                    }
                    if(!setValueType && entry.getValue() != null) {
                        setValueType = true;
                        mapTag.putString("valueType", entry.getValue().getClass().getName());
                    }
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public Map readPersistedField(String name, ValueInput tag) {
                ValueInput mapTag = tag.child(name).orElseThrow();
                Map map = Maps.newHashMap();
                ValueInput.ValueInputList list = mapTag.childrenList("map").orElseThrow();
                if(!list.isEmpty()) {
                    NBTClassType keyNBTClassType;
                    Wrapper<NBTClassType> valueNBTClassType = new Wrapper<>(); // Remains null when all map values are null.
                    try {
                        Class<?> keyType = Class.forName(mapTag.getString("keyType").orElseThrow());
                        keyNBTClassType = getType(keyType, map);
                    } catch (ClassNotFoundException e) {
                        CyclopsCoreInstance.MOD.getLoggerHelper().log(Level.WARN, "No class found for NBT type map key '" + mapTag.getString("keyType")
                                + "', this could be a mod error.");
                        return map;
                    }
                    mapTag.getString("valueType").ifPresent(valueTypeString -> {
                        try {
                            Class<?> valueType = Class.forName(valueTypeString);
                            valueNBTClassType.set(getType(valueType, map));
                        } catch (ClassNotFoundException e) {
                            CyclopsCoreInstance.MOD.getLoggerHelper().log(Level.WARN, "No class found for NBT type map value '" + mapTag.getString("valueType")
                                    + "', this could be a mod error.");
                        }
                    });
                    for (ValueInput entryTag : list) {
                        Object key = keyNBTClassType.readPersistedField("k", entryTag);
                        Object value = null;
                        // If the class type is null, this means all map values are null, so
                        // we won't have any problems with just inserting nulls for all values here.
                        // Also check if it has a 'value' tag, since later elements can still be null.
                        Optional<ValueInput> entryTagChild = entryTag.child("v");
                        if(valueNBTClassType != null && entryTagChild.isPresent()) {
                            value = valueNBTClassType.get().readPersistedField("v", entryTagChild.orElseThrow());
                        }
                        map.put(key, value);
                    }
                }
                return map;
            }

            @Override
            public Map getDefaultValue() {
                return Maps.newHashMap();
            }
        });

        NBTYPES.put(Vec3i.class, new NBTClassType<Vec3i>() {

            @Override
            public void writePersistedField(String name, Vec3i object, ValueOutput tag) {
                tag.putIntArray(name, new int[]{object.getX(), object.getY(), object.getZ()});
            }

            @Override
            public Vec3i readPersistedField(String name, ValueInput tag) {
                int[] array = tag.getIntArray(name).orElseThrow();
                return new Vec3i(array[0], array[1], array[2]);
            }

            @Override
            public Vec3i getDefaultValue() {
                return IModHelpers.get().getLocationHelpers().copyLocation(Vec3i.ZERO);
            }
        });

        NBTYPES.put(Vec3.class, new NBTClassType<Vec3>() {

            @Override
            public void writePersistedField(String name, Vec3 object, ValueOutput tag) {
                ValueOutput vec = tag.child(name);
                vec.putDouble("x", object.x);
                vec.putDouble("y", object.y);
                vec.putDouble("z", object.z);
            }

            @Override
            public Vec3 readPersistedField(String name, ValueInput tag) {
                ValueInput vec = tag.child(name).orElseThrow();
                return new Vec3(vec.getDoubleOr("x", 0), vec.getDoubleOr("y", 0), vec.getDoubleOr("z", 0));
            }

            @Override
            public Vec3 getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Pair.class, new NBTClassType<Pair>() {

            @Override
            public void writePersistedField(String name, Pair object, ValueOutput tag) {
                ValueOutput pairTag = tag.child(name);
                ValueOutput leftTag = pairTag.child("left");
                ValueOutput rightTag = pairTag.child("right");
                getType(object.getLeft().getClass(), object).writePersistedField("element", object.getLeft(), leftTag);
                getType(object.getRight().getClass(), object).writePersistedField("element", object.getRight(), rightTag);
                pairTag.putString("leftType", object.getLeft().getClass().getName());
                pairTag.putString("rightType", object.getRight().getClass().getName());
            }

            @Override
            public Pair readPersistedField(String name, ValueInput tag) {
                ValueInput pairTag = tag.child(name).orElseThrow();
                ValueInput leftTag = pairTag.child("left").orElseThrow();
                ValueInput rightTag = pairTag.child("right").orElseThrow();

                NBTClassType leftElementNBTClassType;
                try {
                    Class<?> elementType = Class.forName(pairTag.getString("leftType").orElseThrow());
                    leftElementNBTClassType = getType(elementType, Pair.class);
                } catch (ClassNotFoundException e) {
                    CyclopsCoreInstance.MOD.getLoggerHelper().log(Level.WARN, "No class found for NBT type Pair left element '" + pairTag.getString("leftType")
                            + "', this could be a mod error.");
                    return Pair.of(null, null);
                }

                NBTClassType rightElementNBTClassType;
                try {
                    Class<?> elementType = Class.forName(pairTag.getString("rightType").orElseThrow());
                    rightElementNBTClassType = getType(elementType, Pair.class);
                } catch (ClassNotFoundException e) {
                    CyclopsCoreInstance.MOD.getLoggerHelper().log(Level.WARN, "No class found for NBT type Pair right element '" + pairTag.getString("rightType")
                            + "', this could be a mod error.");
                    return Pair.of(null, null);
                }

                Object left = leftElementNBTClassType.readPersistedField("element", leftTag);
                Object right = rightElementNBTClassType.readPersistedField("element", rightTag);
                return Pair.of(left, right);
            }

            @Override
            public Pair getDefaultValue() {
                return Pair.of(null, null);
            }
        });

        NBTYPES.put(ItemStack.class, new NBTClassType<ItemStack>() {
            @Override
            public void writePersistedField(String name, ItemStack object, ValueOutput tag) {
                if (object != null) {
                    tag.store(name, ItemStack.OPTIONAL_CODEC, object);
                }
            }

            @Override
            public ItemStack readPersistedField(String name, ValueInput tag) {
                return tag.read(name, ItemStack.OPTIONAL_CODEC).orElseThrow();
            }

            @Override
            public ItemStack getDefaultValue() {
                return null;
            }
        });
        NBTYPES.put(Component.class, new NBTClassType<Component>() {
            @Override
            public void writePersistedField(String name, Component object, ValueOutput tag) {
                if (object != null) {
                    tag.store(name, ComponentSerialization.CODEC, object);
                }
            }

            @Override
            public Component readPersistedField(String name, ValueInput tag) {
                return tag.read(name, ComponentSerialization.CODEC).orElseThrow();
            }

            @Override
            public Component getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(EnumFacingMap.class, new NBTClassType<EnumFacingMap>() {

            @SuppressWarnings("unchecked")
            @Override
            public void writePersistedField(String name, EnumFacingMap object, ValueOutput tag) {
                ValueOutput mapTag = tag.child(name);
                ValueOutput.ValueOutputList list = mapTag.childrenList("map");
                boolean setValueType = false;
                for(Map.Entry entry : (Set<Map.Entry>) object.entrySet()) {
                    ValueOutput entryTag = list.addChild();
                    entryTag.putInt("k", ((Direction) entry.getKey()).ordinal());
                    if(entry.getValue() != null) {
                        getType(entry.getValue().getClass(), object).writePersistedField("v", entry.getValue(), entryTag.child("v"));
                    }

                    if(!setValueType && entry.getValue() != null) {
                        setValueType = true;
                        mapTag.putString("valueType", entry.getValue().getClass().getName());
                    }
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public EnumFacingMap readPersistedField(String name, ValueInput tag) {
                ValueInput mapTag = tag.child(name).orElseThrow();
                EnumFacingMap map = EnumFacingMap.newMap();
                ValueInput.ValueInputList list = mapTag.childrenList("map").orElseThrow();
                if(!list.isEmpty()) {
                    Wrapper<NBTClassType> valueNBTClassType = new Wrapper<>(); // Remains null when all map values are null.
                    mapTag.getString("valueType").ifPresent(valueTypeString -> {
                        try {
                            Class<?> valueType = Class.forName(valueTypeString);
                            valueNBTClassType.set(getType(valueType, map));
                        } catch (ClassNotFoundException e) {
                            CyclopsCoreInstance.MOD.getLoggerHelper().log(Level.WARN, "No class found for NBT type map value '" + mapTag.getString("valueType")
                                    + "', this could be a mod error.");
                        }
                    });
                    for (ValueInput entryTag : list) {
                        Direction key = Direction.values()[entryTag.getInt("k").orElseThrow()];
                        Object value = null;
                        // If the class type is null, this means all map values are null, so
                        // we won't have any problems with just inserting nulls for all values here.
                        // Also check if it has a 'value' tag, since later elements can still be null.
                        Optional<ValueInput> entryTagChild = entryTag.child("v");
                        if(valueNBTClassType != null && entryTagChild.isPresent()) {
                            value = valueNBTClassType.get().readPersistedField("v", entryTagChild.orElseThrow());
                        }
                        map.put(key, value);
                    }
                }
                return map;
            }

            @Override
            public EnumFacingMap getDefaultValue() {
                return EnumFacingMap.newMap();
            }
        });
    }

    /**
     * Get the serialization class for the given object.
     * @param clazz The class of the object.
     * @param <T> The object type
     * @return The serialization class.
     */
    public static <T> NBTClassType<T> getClassType(Class<T> clazz) {
        return (NBTClassType<T>) NBTYPES.get(clazz);
    }

    /**
     * Write the given object to NBT.
     *
     * @param <T>      The class type.
     * @param <I>      The object type.
     * @param clazz    The class of the object.
     * @param name     The NBT key name to write to.
     * @param instance The instance to serialize.
     * @param tag      The NBT tag to write in.
     */
    public static <T, I extends T> void writeNbt(Class<T> clazz, String name, I instance, ValueOutput tag) {
        NBTClassType<T> serializationClass = getClassType(clazz);
        if (serializationClass == null) {
            throw new RuntimeException("No valid NBT serialization was found for " + instance + " of type " + clazz);
        }
        serializationClass.writePersistedField(name, instance, tag);
    }

    /**
     * Read an object from NBT.
     *
     * @param <T>      The class type.
     * @param clazz    The class of the object.
     * @param name     The NBT key name to read from.
     * @param tag      The NBT tag to read in.
     * @return The read object.
     */
    public static <T> T readNbt(Class<T> clazz, String name, ValueInput tag) {
        NBTClassType<T> serializationClass = getClassType(clazz);
        if (serializationClass == null) {
            throw new RuntimeException("No valid NBT serialization was found type " + clazz);
        }
        return serializationClass.readPersistedField(name, tag);
    }

    private static boolean isImplementsInterface(Class<?> clazz, Class<?> interfaceClazz) {
        return interfaceClazz.isAssignableFrom(clazz);
    }

    private static NBTClassType getTypeSilent(Class<?> type) {
        NBTClassType action = NBTClassType.NBTYPES.get(type);
        if(action == null) {
            for(Class<?> iface : type.getInterfaces()) {
                action = NBTClassType.NBTYPES.get(iface);
                if(action != null) {
                    return action;
                }
            }
            Class<?> superClass = type.getSuperclass();
            if(superClass != null) {
                return getTypeSilent(superClass);
            } else {
                return null;
            }
        }
        return action;
    }

    public static NBTClassType getType(Class<?> type, Object target) {
        // Add special logic for INBTSerializable's
        if(isImplementsInterface(type, INBTSerializable.class)) {
            return new INBTSerializable.SelfNBTClassType(type);
        } else {
            NBTClassType<?> action = getTypeSilent(type);
            if (action == null) {
                throw new RuntimeException("No NBT persist action found for type " + type.getName()
                        + " or any of its parents and interfaces in class " + target.getClass() + " for target object "
                        + target + ".");
            }
            return action;
        }
    }

    /**
     * Perform a field persist action.
     *
     * @param provider             The provider that has the field.
     * @param field                The field to persist or read.
     * @param tag                  The tag compound to read or write to.
     */
    public static void performActionForField(INBTProvider provider, Field field, Either<ValueInput, ValueOutput> tag) {
        Class<?> type = field.getType();
        String fieldName = field.getName();

        // Make editable if it was not editable before.
        boolean wasAccessible = field.isAccessible();
        if (!wasAccessible) {
            field.setAccessible(true);
        }

        // Get a non-null action
        getType(type, provider).persistedFieldAction(provider, field, tag);
    }

    /**
     * Called to read or write a field.
     *
     * @param provider             The provider that has the field.
     * @param field                The field to persist or read.
     * @param valueIo              The tag compound to read or write to.
     * @throws IllegalArgumentException Argument exception;
     */
    @SuppressWarnings("unchecked")
    public void persistedFieldAction(INBTProvider provider, Field field, Either<ValueInput, ValueOutput> valueIo) {
        String name = field.getName();
        NBTPersist annotation = field.getAnnotation(NBTPersist.class);
        boolean useDefaultValue = annotation.useDefaultValue();
        Object castTile = field.getDeclaringClass().cast(provider);

        valueIo.ifRight(output -> {
            try {
                field.setAccessible(true); // At least one coremod seems to reset this for some reason, so force enable it again.
                T object = (T) field.get(castTile);
                if(object != null) {
                    try {
                        writePersistedField(name, object, output);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Something went from with the field " + field.getName() + " in " + castTile + ": " + e.getMessage());
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Can not write the field " + field.getName() + " in " + castTile + " since it does not exist. " + e.getMessage());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Can not write the field " + field.getName() + " in " + castTile + " since it can not be accessed. " + e.getMessage());
            }
        });
        valueIo.ifLeft(input -> {
            T object = null;
            try {
                try {
                    object = readPersistedField(name, input);
                    field.setAccessible(true); // At least one coremod seems to reset this for some reason, so force enable it again.
                    field.set(castTile, object);
                } catch(NoSuchElementException error) {
                    if (useDefaultValue) {
                        object = getDefaultValue();
                        field.setAccessible(true); // At least one coremod seems to reset this for some reason, so force enable it again.
                        field.set(castTile, object);
                    }
                }
            }  catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new RuntimeException("Can not read the field " + field.getName() + " as " + object + " in " + castTile + " since it does not exist OR there is a class mismatch. " + e.getMessage());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Can not read the field " + field.getName() + " as " + object + " in " + castTile + " since it can not be accessed. " + e.getMessage());
            }
        });
    }

    public abstract void writePersistedField(String name, T object, ValueOutput tag);
    public abstract T readPersistedField(String name, ValueInput tag);
    public abstract T getDefaultValue();

    private abstract static class CollectionNBTClassType<C extends Collection> extends NBTClassType<C> {

        protected abstract C createNewCollection();

        @Override
        public C getDefaultValue() {
            return createNewCollection();
        }

        @SuppressWarnings("unchecked")
        @Override
        public void writePersistedField(String name, C object, ValueOutput tag) {
            ValueOutput collectionTag = tag.child(name);
            ValueOutput.ValueOutputList list = collectionTag.childrenList("collection");
            boolean setTypes = false;
            for(Object element : object) {
                ValueOutput elementTag = list.addChild();
                getType(element.getClass(), object).writePersistedField("element", element, elementTag);

                if (!setTypes) {
                    setTypes = true;
                    collectionTag.putString("elementType", element.getClass().getName());
                }
            }
        }

        @Override
        public C readPersistedField(String name, ValueInput tag) {
            ValueInput collectionTag = tag.child(name).orElseThrow();
            C collection = createNewCollection();
            ValueInput.ValueInputList list = collectionTag.childrenList("collection").orElseThrow();
            if(!list.isEmpty()) {
                NBTClassType elementNBTClassType;
                try {
                    Class<?> elementType = Class.forName(collectionTag.getString("elementType").orElseThrow());
                    elementNBTClassType = getType(elementType, collection);
                } catch (ClassNotFoundException e) {
                    CyclopsCoreInstance.MOD.getLoggerHelper().log(Level.WARN, "No class found for NBT type collection element '" + collectionTag.getString("elementType")
                            + "', this could be a mod error.");
                    return collection;
                }
                for (ValueInput entryTag : list) {
                    collection.add(elementNBTClassType.readPersistedField("element", entryTag));
                }
            }
            return collection;
        }
    }

}
