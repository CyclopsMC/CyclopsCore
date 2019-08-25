package org.cyclops.cyclopscore.persist.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.datastructure.EnumFacingMap;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Types of NBT field classes used for persistence of fields in {@link org.cyclops.cyclopscore.tileentity.CyclopsTileEntity}.
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
            public void writePersistedField(String name, Integer object, CompoundNBT tag) {
                tag.putInt(name, object);
            }

            @Override
            public Integer readPersistedField(String name, CompoundNBT tag) {
                return tag.getInt(name);
            }

            @Override
            public Integer getDefaultValue() {
                return 0;
            }
        });
        NBTYPES.put(int.class, NBTYPES.get(Integer.class));
        
        NBTYPES.put(Float.class, new NBTClassType<Float>() {

            @Override
            public void writePersistedField(String name, Float object, CompoundNBT tag) {
                tag.putFloat(name, object);
            }

            @Override
            public Float readPersistedField(String name, CompoundNBT tag) {
                return tag.getFloat(name);
            }

            @Override
            public Float getDefaultValue() {
                return 0F;
            }
        });
        NBTYPES.put(float.class, NBTYPES.get(Float.class));
        
        NBTYPES.put(Boolean.class, new NBTClassType<Boolean>() {

            @Override
            public void writePersistedField(String name, Boolean object, CompoundNBT tag) {
                tag.putBoolean(name, object);
            }

            @Override
            public Boolean readPersistedField(String name, CompoundNBT tag) {
                return tag.getBoolean(name);
            }

            @Override
            public Boolean getDefaultValue() {
                return false;
            }
        });
        NBTYPES.put(boolean.class, NBTYPES.get(Boolean.class));
        
        NBTYPES.put(String.class, new NBTClassType<String>() {

            @Override
            public void writePersistedField(String name, String object, CompoundNBT tag) {
            	if(object != null && !object.isEmpty()) {
            		tag.putString(name, object);
            	}
            }

            @Override
            public String readPersistedField(String name, CompoundNBT tag) {
                return tag.getString(name);
            }

            @Override
            public String getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Direction.class, new NBTClassType<Direction>() {
            @Override
            public void writePersistedField(String name, Direction object, CompoundNBT tag) {
                tag.putInt(name, object.ordinal());
            }

            @Override
            public Direction readPersistedField(String name, CompoundNBT tag) {
                return Direction.values()[tag.getInt(name)];
            }

            @Override
            public Direction getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Fluid.class, new NBTClassType<Fluid>() {
            @Override
            public void writePersistedField(String name, Fluid object, CompoundNBT tag) {
                tag.putString(name, object.getRegistryName().toString());
            }

            @Override
            public Fluid readPersistedField(String name, CompoundNBT tag) {
                String fluidName = tag.getString(name);
                return ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryCreate(fluidName));
            }

            @Override
            public Fluid getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(FluidStack.class, new NBTClassType<FluidStack>() {
            @Override
            public void writePersistedField(String name, @Nullable FluidStack object, CompoundNBT tag) {
                if (object != null) {
                    CompoundNBT subTag = new CompoundNBT();
                    object.writeToNBT(subTag);
                    tag.put(name, subTag);
                }
            }

            @Override
            public FluidStack readPersistedField(String name, CompoundNBT tag) {
                return FluidStack.loadFluidStackFromNBT(tag.getCompound(name));
            }

            @Override
            public FluidStack getDefaultValue() {
                return null;
            }
        });
        
        NBTYPES.put(CompoundNBT.class, new NBTClassType<CompoundNBT>() {

            @Override
            public void writePersistedField(String name, CompoundNBT object, CompoundNBT tag) {
                tag.put(name, object);
            }

            @Override
            public CompoundNBT readPersistedField(String name, CompoundNBT tag) {
                return tag.getCompound(name);
            }

            @Override
            public CompoundNBT getDefaultValue() {
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
            public void writePersistedField(String name, Map object, CompoundNBT tag) {
                CompoundNBT mapTag = new CompoundNBT();
                ListNBT list = new ListNBT();
                boolean setKeyType = false;
                boolean setValueType = false;
                for(Map.Entry entry : (Set<Map.Entry>) object.entrySet()) {
                    CompoundNBT entryTag = new CompoundNBT();
                    getType(entry.getKey().getClass(), object).writePersistedField("key", entry.getKey(), entryTag);
                    if(entry.getValue() != null) {
                        getType(entry.getValue().getClass(), object).writePersistedField("value", entry.getValue(), entryTag);
                    }
                    list.add(entryTag);

                    if(!setKeyType) {
                        setKeyType = true;
                        mapTag.putString("keyType", entry.getKey().getClass().getName());
                    }
                    if(!setValueType && entry.getValue() != null) {
                        setValueType = true;
                        mapTag.putString("valueType", entry.getValue().getClass().getName());
                    }
                }
                mapTag.put("map", list);
                tag.put(name, mapTag);
            }

            @SuppressWarnings("unchecked")
            @Override
            public Map readPersistedField(String name, CompoundNBT tag) {
                CompoundNBT mapTag = tag.getCompound(name);
                Map map = Maps.newHashMap();
                ListNBT list = mapTag.getList("map", Constants.NBT.TAG_COMPOUND);
                if(list.size() > 0) {
                    NBTClassType keyNBTClassType;
                    NBTClassType valueNBTClassType = null; // Remains null when all map values are null.
                    try {
                        Class<?> keyType = Class.forName(mapTag.getString("keyType"));
                        keyNBTClassType = getType(keyType, map);
                    } catch (ClassNotFoundException e) {
                        CyclopsCore.clog(Level.WARN, "No class found for NBT type map key '" + mapTag.getString("keyType")
                                + "', this could be a mod error.");
                        return map;
                    }
                    if(mapTag.contains("valueType")) {
                        try {
                            Class<?> valueType = Class.forName(mapTag.getString("valueType"));
                            valueNBTClassType = getType(valueType, map);
                        } catch (ClassNotFoundException e) {
                            CyclopsCore.clog(Level.WARN, "No class found for NBT type map value '" + mapTag.getString("valueType")
                                    + "', this could be a mod error.");
                            return map;
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        CompoundNBT entryTag = list.getCompound(i);
                        Object key = keyNBTClassType.readPersistedField("key", entryTag);
                        Object value = null;
                        // If the class type is null, this means all map values are null, so
                        // we won't have any problems with just inserting nulls for all values here.
                        // Also check if it has a 'value' tag, since later elements can still be null.
                        if(valueNBTClassType != null && entryTag.contains("value")) {
                            value = valueNBTClassType.readPersistedField("value", entryTag);
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
            public void writePersistedField(String name, Vec3i object, CompoundNBT tag) {
                tag.putIntArray(name, new int[]{object.getX(), object.getY(), object.getZ()});
            }

            @Override
            public Vec3i readPersistedField(String name, CompoundNBT tag) {
                int[] array = tag.getIntArray(name);
                return new Vec3i(array[0], array[1], array[2]);
            }

            @Override
            public Vec3i getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Vec3d.class, new NBTClassType<Vec3d>() {

            @Override
            public void writePersistedField(String name, Vec3d object, CompoundNBT tag) {
                CompoundNBT vec = new CompoundNBT();
                vec.putDouble("x", object.x);
                vec.putDouble("y", object.y);
                vec.putDouble("z", object.z);
                tag.put(name, vec);
            }

            @Override
            public Vec3d readPersistedField(String name, CompoundNBT tag) {
                CompoundNBT vec = tag.getCompound(name);
                return new Vec3d(vec.getDouble("x"), vec.getDouble("y"), vec.getDouble("z"));
            }

            @Override
            public Vec3d getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Pair.class, new NBTClassType<Pair>() {

            @Override
            public void writePersistedField(String name, Pair object, CompoundNBT tag) {
                CompoundNBT pairTag = new CompoundNBT();
                CompoundNBT leftTag = new CompoundNBT();
                CompoundNBT rightTag = new CompoundNBT();
                getType(object.getLeft().getClass(), object).writePersistedField("element", object.getLeft(), leftTag);
                getType(object.getRight().getClass(), object).writePersistedField("element", object.getRight(), rightTag);
                pairTag.putString("leftType", object.getLeft().getClass().getName());
                pairTag.putString("rightType", object.getRight().getClass().getName());
                pairTag.put("left", leftTag);
                pairTag.put("right", rightTag);
                tag.put(name, pairTag);
            }

            @Override
            public Pair readPersistedField(String name, CompoundNBT tag) {
                CompoundNBT pairTag = tag.getCompound(name);
                CompoundNBT leftTag = pairTag.getCompound("left");
                CompoundNBT rightTag = pairTag.getCompound("right");

                NBTClassType leftElementNBTClassType;
                try {
                    Class<?> elementType = Class.forName(pairTag.getString("leftType"));
                    leftElementNBTClassType = getType(elementType, Pair.class);
                } catch (ClassNotFoundException e) {
                    CyclopsCore.clog(Level.WARN, "No class found for NBT type Pair left element '" + pairTag.getString("leftType")
                            + "', this could be a mod error.");
                    return Pair.of(null, null);
                }

                NBTClassType rightElementNBTClassType;
                try {
                    Class<?> elementType = Class.forName(pairTag.getString("rightType"));
                    rightElementNBTClassType = getType(elementType, Pair.class);
                } catch (ClassNotFoundException e) {
                    CyclopsCore.clog(Level.WARN, "No class found for NBT type Pair right element '" + pairTag.getString("rightType")
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

        NBTYPES.put(DimPos.class, new NBTClassType<DimPos>() {

            @Override
            public void writePersistedField(String name, DimPos object, CompoundNBT tag) {
                CompoundNBT dimPos = new CompoundNBT();
                dimPos.putString("dim", object.getDimension().getRegistryName().toString());
                dimPos.putInt("x", object.getBlockPos().getX());
                dimPos.putInt("y", object.getBlockPos().getY());
                dimPos.putInt("z", object.getBlockPos().getZ());
                tag.put(name, dimPos);
            }

            @Override
            public DimPos readPersistedField(String name, CompoundNBT tag) {
                CompoundNBT dimPos = tag.getCompound(name);
                String dimensionName = dimPos.getString("dim");
                Optional<DimensionType> dimensionType = Registry.DIMENSION_TYPE.getValue(ResourceLocation.tryCreate(dimensionName));
                if (!dimensionType.isPresent()) {
                    CyclopsCore.clog(Level.WARN, String.format("Could not find dimension %s", dimensionName));
                }
                return DimPos.of(dimensionType.orElse(DimensionType.OVERWORLD), new BlockPos(dimPos.getInt("x"), dimPos.getInt("y"), dimPos.getInt("z")));
            }

            @Override
            public DimPos getDefaultValue() {
                return null;
            }
        });
        NBTYPES.put(ItemStack.class, new NBTClassType<ItemStack>() {
            @Override
            public void writePersistedField(String name, ItemStack object, CompoundNBT tag) {
                if (object != null) {
                    tag.put(name, object.copy().write(new CompoundNBT()));
                }
            }

            @Override
            public ItemStack readPersistedField(String name, CompoundNBT tag) {
                return ItemStack.read(tag.getCompound(name));
            }

            @Override
            public ItemStack getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(EnumFacingMap.class, new NBTClassType<EnumFacingMap>() {

            @SuppressWarnings("unchecked")
            @Override
            public void writePersistedField(String name, EnumFacingMap object, CompoundNBT tag) {
                CompoundNBT mapTag = new CompoundNBT();
                ListNBT list = new ListNBT();
                boolean setValueType = false;
                for(Map.Entry entry : (Set<Map.Entry>) object.entrySet()) {
                    CompoundNBT entryTag = new CompoundNBT();
                    entryTag.putInt("key", ((Direction) entry.getKey()).ordinal());
                    if(entry.getValue() != null) {
                        getType(entry.getValue().getClass(), object).writePersistedField("value", entry.getValue(), entryTag);
                    }
                    list.add(entryTag);

                    if(!setValueType && entry.getValue() != null) {
                        setValueType = true;
                        mapTag.putString("valueType", entry.getValue().getClass().getName());
                    }
                }
                mapTag.put("map", list);
                tag.put(name, mapTag);
            }

            @SuppressWarnings("unchecked")
            @Override
            public EnumFacingMap readPersistedField(String name, CompoundNBT tag) {
                CompoundNBT mapTag = tag.getCompound(name);
                EnumFacingMap map = EnumFacingMap.newMap();
                ListNBT list = mapTag.getList("map", Constants.NBT.TAG_COMPOUND);
                if(list.size() > 0) {
                    NBTClassType valueNBTClassType = null; // Remains null when all map values are null.
                    if(mapTag.contains("valueType")) {
                        try {
                            Class<?> valueType = Class.forName(mapTag.getString("valueType"));
                            valueNBTClassType = getType(valueType, map);
                        } catch (ClassNotFoundException e) {
                            CyclopsCore.clog(Level.WARN, "No class found for NBT type map value '" + mapTag.getString("valueType")
                                    + "', this could be a mod error.");
                            return map;
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        CompoundNBT entryTag = list.getCompound(i);
                        Direction key = Direction.values()[entryTag.getInt("key")];
                        Object value = null;
                        // If the class type is null, this means all map values are null, so
                        // we won't have any problems with just inserting nulls for all values here.
                        // Also check if it has a 'value' tag, since later elements can still be null.
                        if(valueNBTClassType != null && entryTag.contains("value")) {
                            value = valueNBTClassType.readPersistedField("value", entryTag);
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
     * @param clazz The class of the object.
     * @param name The NBT key name to write to.
     * @param instance The instance to serialize.
     * @param tag The NBT tag to write in.
     * @param <T> The class type.
     * @param <I> The object type.
     */
    public static <T, I extends T> void writeNbt(Class<T> clazz, String name, I instance, CompoundNBT tag) {
        NBTClassType<T> serializationClass = getClassType(clazz);
        if (serializationClass == null) {
            throw new RuntimeException("No valid NBT serialization was found for " + instance + " of type " + clazz);
        }
        serializationClass.writePersistedField(name, instance, tag);
    }

    /**
     * Read an object from NBT.
     * @param clazz The class of the object.
     * @param name The NBT key name to read from.
     * @param tag The NBT tag to read in.
     * @param <T> The class type.
     * @return The read object.
     */
    public static <T> T readNbt(Class<T> clazz, String name, CompoundNBT tag) {
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
     * @param provider The provider that has the field.
     * @param field The field to persist or read.
     * @param tag The tag compound to read or write to.
     * @param write If there should be written, otherwise there will be read.
     */
    public static void performActionForField(INBTProvider provider, Field field, CompoundNBT tag, boolean write) {
        Class<?> type = field.getType();
        String fieldName = field.getName();
        
        // Make editable if it was not editable before.
        boolean wasAccessible = field.isAccessible();
        if (!wasAccessible) {
            try {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(true);
        }

        // Get a non-null action
        NBTClassType<?> action = getType(type, provider);
        try {
            action.persistedFieldAction(provider, field, tag, write);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not access field " + fieldName + " in " + provider.getClass() + " " + e.getMessage());
        }
    }
    
    /**
     * Called to read or write a field.
     * @param provider The provider that has the field.
     * @param field The field to persist or read.
     * @param tag The tag compound to read or write to.
     * @param write If there should be written, otherwise there will be read.
     * @throws IllegalArgumentException Argument exception;
     * @throws IllegalAccessException Access exception;
     */
    @SuppressWarnings("unchecked")
    public void persistedFieldAction(INBTProvider provider, Field field, CompoundNBT tag, boolean write) throws IllegalAccessException {
        String name = field.getName();
        NBTPersist annotation = field.getAnnotation(NBTPersist.class);
        boolean useDefaultValue = annotation.useDefaultValue();
        Object castTile = field.getDeclaringClass().cast(provider);
        if(write) {
            try {
                field.setAccessible(true); // At least one coremod seems to reset this for some reason, so force enable it again.
                T object = (T) field.get(castTile);
                if(object != null) {
                    try {
                        writePersistedField(name, object, tag);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Something went from with the field " + field.getName() + " in " + castTile + ": " + e.getMessage());
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Can not write the field " + field.getName() + " in " + castTile + " since it does not exist. " + e.getMessage());
            }
        } else {
            T object = null;
            try {
                if(tag.contains(name)) {
                    object = readPersistedField(name, tag);
                    field.setAccessible(true); // At least one coremod seems to reset this for some reason, so force enable it again.
                    field.set(castTile, object);
                } else if (useDefaultValue) {
                    object = getDefaultValue();
                    field.setAccessible(true); // At least one coremod seems to reset this for some reason, so force enable it again.
                    field.set(castTile, object);
                }
            }  catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new RuntimeException("Can not read the field " + field.getName() + " as " + object + " in " + castTile + " since it does not exist OR there is a class mismatch. " + e.getMessage());
            }

        }
    }
    
    public abstract void writePersistedField(String name, T object, CompoundNBT tag);
    public abstract T readPersistedField(String name, CompoundNBT tag);
    public abstract T getDefaultValue();

    private abstract static class CollectionNBTClassType<C extends Collection> extends NBTClassType<C> {

        protected abstract C createNewCollection();

        @Override
        public C getDefaultValue() {
            return createNewCollection();
        }

        @SuppressWarnings("unchecked")
        @Override
        public void writePersistedField(String name, C object, CompoundNBT tag) {
            CompoundNBT collectionTag = new CompoundNBT();
            ListNBT list = new ListNBT();
            boolean setTypes = false;
            for(Object element : object) {
                CompoundNBT elementTag = new CompoundNBT();
                getType(element.getClass(), object).writePersistedField("element", element, elementTag);
                list.add(elementTag);

                if (!setTypes) {
                    setTypes = true;
                    collectionTag.putString("elementType", element.getClass().getName());
                }
            }
            collectionTag.put("collection", list);
            tag.put(name, collectionTag);
        }

        @Override
        public C readPersistedField(String name, CompoundNBT tag) {
            CompoundNBT collectionTag = tag.getCompound(name);
            C collection = createNewCollection();
            ListNBT list = collectionTag.getList("collection", Constants.NBT.TAG_COMPOUND);
            if(list.size() > 0) {
                NBTClassType elementNBTClassType;
                try {
                    Class<?> elementType = Class.forName(collectionTag.getString("elementType"));
                    elementNBTClassType = getType(elementType, collection);
                } catch (ClassNotFoundException e) {
                    CyclopsCore.clog(Level.WARN, "No class found for NBT type collection element '" + collectionTag.getString("elementType")
                            + "', this could be a mod error.");
                    return collection;
                }
                for (int i = 0; i < list.size(); i++) {
                    CompoundNBT entryTag = list.getCompound(i);
                    Object element = elementNBTClassType.readPersistedField("element", entryTag);
                    collection.add(element);
                }
            }
            return collection;
        }
    }
    
}
