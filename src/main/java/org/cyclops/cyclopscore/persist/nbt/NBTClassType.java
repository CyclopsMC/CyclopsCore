package org.cyclops.cyclopscore.persist.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.datastructure.EnumFacingMap;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

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
            public void writePersistedField(String name, Integer object, NBTTagCompound tag) {
                tag.setInteger(name, object);
            }

            @Override
            public Integer readPersistedField(String name, NBTTagCompound tag) {
                return tag.getInteger(name);
            }

            @Override
            public Integer getDefaultValue() {
                return 0;
            }
        });
        NBTYPES.put(int.class, NBTYPES.get(Integer.class));
        
        NBTYPES.put(Float.class, new NBTClassType<Float>() {

            @Override
            public void writePersistedField(String name, Float object, NBTTagCompound tag) {
                tag.setFloat(name, object);
            }

            @Override
            public Float readPersistedField(String name, NBTTagCompound tag) {
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
            public void writePersistedField(String name, Boolean object, NBTTagCompound tag) {
                tag.setBoolean(name, object);
            }

            @Override
            public Boolean readPersistedField(String name, NBTTagCompound tag) {
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
            public void writePersistedField(String name, String object, NBTTagCompound tag) {
            	if(object != null && !object.isEmpty()) {
            		tag.setString(name, object);
            	}
            }

            @Override
            public String readPersistedField(String name, NBTTagCompound tag) {
                return tag.getString(name);
            }

            @Override
            public String getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(EnumFacing.class, new NBTClassType<EnumFacing>() {
            @Override
            public void writePersistedField(String name, EnumFacing object, NBTTagCompound tag) {
                tag.setInteger(name, object.ordinal());
            }

            @Override
            public EnumFacing readPersistedField(String name, NBTTagCompound tag) {
                return EnumFacing.values()[tag.getInteger(name)];
            }

            @Override
            public EnumFacing getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Fluid.class, new NBTClassType<Fluid>() {
            @Override
            public void writePersistedField(String name, Fluid object, NBTTagCompound tag) {
                tag.setString(name, object.getName());
            }

            @Override
            public Fluid readPersistedField(String name, NBTTagCompound tag) {
                String fluidName = tag.getString(name);
                return FluidRegistry.getFluid(fluidName);
            }

            @Override
            public Fluid getDefaultValue() {
                return null;
            }
        });
        
        NBTYPES.put(NBTTagCompound.class, new NBTClassType<NBTTagCompound>() {

            @Override
            public void writePersistedField(String name, NBTTagCompound object, NBTTagCompound tag) {
                tag.setTag(name, object);
            }

            @Override
            public NBTTagCompound readPersistedField(String name, NBTTagCompound tag) {
                return tag.getCompoundTag(name);
            }

            @Override
            public NBTTagCompound getDefaultValue() {
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
            public void writePersistedField(String name, Map object, NBTTagCompound tag) {
                NBTTagCompound mapTag = new NBTTagCompound();
                NBTTagList list = new NBTTagList();
                boolean setKeyType = false;
                boolean setValueType = false;
                for(Map.Entry entry : (Set<Map.Entry>) object.entrySet()) {
                    NBTTagCompound entryTag = new NBTTagCompound();
                    getType(entry.getKey().getClass(), object).writePersistedField("key", entry.getKey(), entryTag);
                    if(entry.getValue() != null) {
                        getType(entry.getValue().getClass(), object).writePersistedField("value", entry.getValue(), entryTag);
                    }
                    list.appendTag(entryTag);

                    if(!setKeyType) {
                        setKeyType = true;
                        mapTag.setString("keyType", entry.getKey().getClass().getName());
                    }
                    if(!setValueType && entry.getValue() != null) {
                        setValueType = true;
                        mapTag.setString("valueType", entry.getValue().getClass().getName());
                    }
                }
                mapTag.setTag("map", list);
                tag.setTag(name, mapTag);
            }

            @SuppressWarnings("unchecked")
            @Override
            public Map readPersistedField(String name, NBTTagCompound tag) {
                NBTTagCompound mapTag = tag.getCompoundTag(name);
                Map map = Maps.newHashMap();
                NBTTagList list = mapTag.getTagList("map", MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());
                if(list.tagCount() > 0) {
                    NBTClassType keyNBTClassType;
                    NBTClassType valueNBTClassType = null; // Remains null when all map values are null.
                    try {
                        Class keyType = Class.forName(mapTag.getString("keyType"));
                        keyNBTClassType = getType(keyType, map);
                    } catch (ClassNotFoundException e) {
                        CyclopsCore.clog(Level.WARN, "No class found for NBT type map key '" + mapTag.getString("keyType")
                                + "', this could be a mod error.");
                        return map;
                    }
                    if(mapTag.hasKey("valueType")) {
                        try {
                            Class valueType = Class.forName(mapTag.getString("valueType"));
                            valueNBTClassType = getType(valueType, map);
                        } catch (ClassNotFoundException e) {
                            CyclopsCore.clog(Level.WARN, "No class found for NBT type map value '" + mapTag.getString("valueType")
                                    + "', this could be a mod error.");
                            return map;
                        }
                    }
                    for (int i = 0; i < list.tagCount(); i++) {
                        NBTTagCompound entryTag = list.getCompoundTagAt(i);
                        Object key = keyNBTClassType.readPersistedField("key", entryTag);
                        Object value = null;
                        // If the class type is null, this means all map values are null, so
                        // we won't have any problems with just inserting nulls for all values here.
                        // Also check if it has a 'value' tag, since later elements can still be null.
                        if(valueNBTClassType != null && entryTag.hasKey("value")) {
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
            public void writePersistedField(String name, Vec3i object, NBTTagCompound tag) {
                tag.setIntArray(name, new int[]{object.getX(), object.getY(), object.getZ()});
            }

            @Override
            public Vec3i readPersistedField(String name, NBTTagCompound tag) {
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
            public void writePersistedField(String name, Vec3d object, NBTTagCompound tag) {
                NBTTagCompound vec = new NBTTagCompound();
                vec.setDouble("x", object.xCoord);
                vec.setDouble("y", object.yCoord);
                vec.setDouble("z", object.zCoord);
                tag.setTag(name, vec);
            }

            @Override
            public Vec3d readPersistedField(String name, NBTTagCompound tag) {
                NBTTagCompound vec = tag.getCompoundTag(name);
                return new Vec3d(vec.getDouble("x"), vec.getDouble("y"), vec.getDouble("z"));
            }

            @Override
            public Vec3d getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Pair.class, new NBTClassType<Pair>() {

            @Override
            public void writePersistedField(String name, Pair object, NBTTagCompound tag) {
                NBTTagCompound pairTag = new NBTTagCompound();
                NBTTagCompound leftTag = new NBTTagCompound();
                NBTTagCompound rightTag = new NBTTagCompound();
                getType(object.getLeft().getClass(), object).writePersistedField("element", object.getLeft(), leftTag);
                getType(object.getRight().getClass(), object).writePersistedField("element", object.getRight(), rightTag);
                pairTag.setString("leftType", object.getLeft().getClass().getName());
                pairTag.setString("rightType", object.getRight().getClass().getName());
                pairTag.setTag("left", leftTag);
                pairTag.setTag("right", rightTag);
                tag.setTag(name, pairTag);
            }

            @Override
            public Pair readPersistedField(String name, NBTTagCompound tag) {
                NBTTagCompound pairTag = tag.getCompoundTag(name);
                NBTTagCompound leftTag = pairTag.getCompoundTag("left");
                NBTTagCompound rightTag = pairTag.getCompoundTag("right");

                NBTClassType leftElementNBTClassType;
                try {
                    Class elementType = Class.forName(pairTag.getString("leftType"));
                    leftElementNBTClassType = getType(elementType, Pair.class);
                } catch (ClassNotFoundException e) {
                    CyclopsCore.clog(Level.WARN, "No class found for NBT type Pair left element '" + pairTag.getString("leftType")
                            + "', this could be a mod error.");
                    return Pair.of(null, null);
                }

                NBTClassType rightElementNBTClassType;
                try {
                    Class elementType = Class.forName(pairTag.getString("rightType"));
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
            public void writePersistedField(String name, DimPos object, NBTTagCompound tag) {
                NBTTagCompound dimPos = new NBTTagCompound();
                dimPos.setDouble("dim", object.getDimensionId());
                dimPos.setInteger("x", object.getBlockPos().getX());
                dimPos.setInteger("y", object.getBlockPos().getY());
                dimPos.setInteger("z", object.getBlockPos().getZ());
                tag.setTag(name, dimPos);
            }

            @SideOnly(Side.CLIENT)
            protected World getClientWorld() {
                return Minecraft.getMinecraft().theWorld;
            }

            @Override
            public DimPos readPersistedField(String name, NBTTagCompound tag) {
                NBTTagCompound dimPos = tag.getCompoundTag(name);
                int dim = dimPos.getInteger("dim");
                World world;
                if(!MinecraftHelpers.isClientSide()) {
                    if (FMLCommonHandler.instance().getMinecraftServerInstance().worldServers.length >= dim) {
                        dim = 0;
                    }
                    world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[dim];
                } else {
                    world = getClientWorld();
                    if(world.provider.getDimension() != dim) {
                        CyclopsCore.clog(Level.WARN, String.format("Tried to fetch dimension %s at client-side while dimension %s was loaded.", dim, world.provider.getDimension()));
                    }
                }
                return DimPos.of(world, new BlockPos(dimPos.getInteger("x"), dimPos.getInteger("y"), dimPos.getInteger("z")));
            }

            @Override
            public DimPos getDefaultValue() {
                return null;
            }
        });
        NBTYPES.put(ItemStack.class, new NBTClassType<ItemStack>() {
            @Override
            public void writePersistedField(String name, ItemStack object, NBTTagCompound tag) {
                if (object != null) {
                    tag.setTag(name, object.copy().writeToNBT(new NBTTagCompound()));
                }
            }

            @Override
            public ItemStack readPersistedField(String name, NBTTagCompound tag) {
                return ItemStack.loadItemStackFromNBT(tag.getCompoundTag(name));
            }

            @Override
            public ItemStack getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(EnumFacingMap.class, new NBTClassType<EnumFacingMap>() {

            @SuppressWarnings("unchecked")
            @Override
            public void writePersistedField(String name, EnumFacingMap object, NBTTagCompound tag) {
                NBTTagCompound mapTag = new NBTTagCompound();
                NBTTagList list = new NBTTagList();
                boolean setValueType = false;
                for(Map.Entry entry : (Set<Map.Entry>) object.entrySet()) {
                    NBTTagCompound entryTag = new NBTTagCompound();
                    entryTag.setInteger("key", ((EnumFacing) entry.getKey()).ordinal());
                    if(entry.getValue() != null) {
                        getType(entry.getValue().getClass(), object).writePersistedField("value", entry.getValue(), entryTag);
                    }
                    list.appendTag(entryTag);

                    if(!setValueType && entry.getValue() != null) {
                        setValueType = true;
                        mapTag.setString("valueType", entry.getValue().getClass().getName());
                    }
                }
                mapTag.setTag("map", list);
                tag.setTag(name, mapTag);
            }

            @SuppressWarnings("unchecked")
            @Override
            public EnumFacingMap readPersistedField(String name, NBTTagCompound tag) {
                NBTTagCompound mapTag = tag.getCompoundTag(name);
                EnumFacingMap map = EnumFacingMap.newMap();
                NBTTagList list = mapTag.getTagList("map", MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());
                if(list.tagCount() > 0) {
                    NBTClassType valueNBTClassType = null; // Remains null when all map values are null.
                    if(mapTag.hasKey("valueType")) {
                        try {
                            Class valueType = Class.forName(mapTag.getString("valueType"));
                            valueNBTClassType = getType(valueType, map);
                        } catch (ClassNotFoundException e) {
                            CyclopsCore.clog(Level.WARN, "No class found for NBT type map value '" + mapTag.getString("valueType")
                                    + "', this could be a mod error.");
                            return map;
                        }
                    }
                    for (int i = 0; i < list.tagCount(); i++) {
                        NBTTagCompound entryTag = list.getCompoundTagAt(i);
                        EnumFacing key = EnumFacing.VALUES[entryTag.getInteger("key")];
                        Object value = null;
                        // If the class type is null, this means all map values are null, so
                        // we won't have any problems with just inserting nulls for all values here.
                        // Also check if it has a 'value' tag, since later elements can still be null.
                        if(valueNBTClassType != null && entryTag.hasKey("value")) {
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
    public static <T, I extends T> void writeNbt(Class<T> clazz, String name, I instance, NBTTagCompound tag) {
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
    public static <T> T readNbt(Class<T> clazz, String name, NBTTagCompound tag) {
        NBTClassType<T> serializationClass = getClassType(clazz);
        if (serializationClass == null) {
            throw new RuntimeException("No valid NBT serialization was found type " + clazz);
        }
        return serializationClass.readPersistedField(name, tag);
    }
    
    private static boolean isImplementsInterface(Class<?> clazz, Class<?> interfaceClazz) {
    	try {
    		clazz.asSubclass(interfaceClazz);
    	} catch (ClassCastException e) {
    		return false;
    	}
    	return true;
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
    public static void performActionForField(INBTProvider provider, Field field, NBTTagCompound tag, boolean write) {
        Class<?> type = field.getType();
        String fieldName = field.getName();
        
        // Make editable, will set back to the original at the end of this call.
        boolean wasAccessible = field.isAccessible();
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);

        // Get a non-null action
        NBTClassType<?> action = getType(type, provider);
        try {
            action.persistedFieldAction(provider, field, tag, write);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not access field " + fieldName + " in " + provider.getClass() + " " + e.getMessage());
        }
        
        field.setAccessible(wasAccessible);
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
    public void persistedFieldAction(INBTProvider provider, Field field, NBTTagCompound tag, boolean write) throws IllegalAccessException {
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
                if(tag.hasKey(name)) {
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
    
    public abstract void writePersistedField(String name, T object, NBTTagCompound tag);
    public abstract T readPersistedField(String name, NBTTagCompound tag);
    public abstract T getDefaultValue();

    private abstract static class CollectionNBTClassType<C extends Collection> extends NBTClassType<C> {

        protected abstract C createNewCollection();

        @Override
        public C getDefaultValue() {
            return createNewCollection();
        }

        @SuppressWarnings("unchecked")
        @Override
        public void writePersistedField(String name, C object, NBTTagCompound tag) {
            NBTTagCompound collectionTag = new NBTTagCompound();
            NBTTagList list = new NBTTagList();
            boolean setTypes = false;
            for(Object element : object) {
                NBTTagCompound elementTag = new NBTTagCompound();
                getType(element.getClass(), object).writePersistedField("element", element, elementTag);
                list.appendTag(elementTag);

                if (!setTypes) {
                    setTypes = true;
                    collectionTag.setString("elementType", element.getClass().getName());
                }
            }
            collectionTag.setTag("collection", list);
            tag.setTag(name, collectionTag);
        }

        @Override
        public C readPersistedField(String name, NBTTagCompound tag) {
            NBTTagCompound collectionTag = tag.getCompoundTag(name);
            C collection = createNewCollection();
            NBTTagList list = collectionTag.getTagList("collection", MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());
            if(list.tagCount() > 0) {
                NBTClassType elementNBTClassType;
                try {
                    Class elementType = Class.forName(collectionTag.getString("elementType"));
                    elementNBTClassType = getType(elementType, collection);
                } catch (ClassNotFoundException e) {
                    CyclopsCore.clog(Level.WARN, "No class found for NBT type collection element '" + collectionTag.getString("elementType")
                            + "', this could be a mod error.");
                    return collection;
                }
                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound entryTag = list.getCompoundTagAt(i);
                    Object element = elementNBTClassType.readPersistedField("element", entryTag);
                    collection.add(element);
                }
            }
            return collection;
        }
    }
    
}
