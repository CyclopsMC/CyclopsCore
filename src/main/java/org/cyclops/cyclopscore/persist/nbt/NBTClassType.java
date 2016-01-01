package org.cyclops.cyclopscore.persist.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.datastructure.DimPos;
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
    public static Map<Class<?>, NBTClassType<?>> NBTYPES = new HashMap<Class<?>, NBTClassType<?>>(); 
    static {
        NBTYPES.put(Integer.class, new NBTClassType<Integer>() {

            @Override
            protected void writePersistedField(String name, Integer object, NBTTagCompound tag) {
                tag.setInteger(name, object);
            }

            @Override
            protected Integer readPersistedField(String name, NBTTagCompound tag) {
                return tag.getInteger(name);
            }

            @Override
            protected Integer getDefaultValue() {
                return 0;
            }
        });
        NBTYPES.put(int.class, NBTYPES.get(Integer.class));
        
        NBTYPES.put(Float.class, new NBTClassType<Float>() {

            @Override
            protected void writePersistedField(String name, Float object, NBTTagCompound tag) {
                tag.setFloat(name, object);
            }

            @Override
            protected Float readPersistedField(String name, NBTTagCompound tag) {
                return tag.getFloat(name);
            }

            @Override
            protected Float getDefaultValue() {
                return 0F;
            }
        });
        NBTYPES.put(float.class, NBTYPES.get(Float.class));
        
        NBTYPES.put(Boolean.class, new NBTClassType<Boolean>() {

            @Override
            protected void writePersistedField(String name, Boolean object, NBTTagCompound tag) {
                tag.setBoolean(name, object);
            }

            @Override
            protected Boolean readPersistedField(String name, NBTTagCompound tag) {
                return tag.getBoolean(name);
            }

            @Override
            protected Boolean getDefaultValue() {
                return false;
            }
        });
        NBTYPES.put(boolean.class, NBTYPES.get(Boolean.class));
        
        NBTYPES.put(String.class, new NBTClassType<String>() {

            @Override
            protected void writePersistedField(String name, String object, NBTTagCompound tag) {
            	if(object != null && !object.isEmpty()) {
            		tag.setString(name, object);
            	}
            }

            @Override
            protected String readPersistedField(String name, NBTTagCompound tag) {
                return tag.getString(name);
            }

            @Override
            protected String getDefaultValue() {
                return null;
            }
        });
        
        NBTYPES.put(NBTTagCompound.class, new NBTClassType<NBTTagCompound>() {

            @Override
            protected void writePersistedField(String name, NBTTagCompound object, NBTTagCompound tag) {
                tag.setTag(name, object);
            }

            @Override
            protected NBTTagCompound readPersistedField(String name, NBTTagCompound tag) {
                return tag.getCompoundTag(name);
            }

            @Override
            protected NBTTagCompound getDefaultValue() {
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
            protected void writePersistedField(String name, Map object, NBTTagCompound tag) {
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
            protected Map readPersistedField(String name, NBTTagCompound tag) {
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
            protected Map getDefaultValue() {
                return Maps.newHashMap();
            }
        });

        NBTYPES.put(Vec3i.class, new NBTClassType<Vec3i>() {

            @Override
            protected void writePersistedField(String name, Vec3i object, NBTTagCompound tag) {
                tag.setIntArray(name, new int[]{object.getX(), object.getY(), object.getZ()});
            }

            @Override
            protected Vec3i readPersistedField(String name, NBTTagCompound tag) {
                int[] array = tag.getIntArray(name);
                return new Vec3i(array[0], array[1], array[2]);
            }

            @Override
            protected Vec3i getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Vec3.class, new NBTClassType<Vec3>() {

            @Override
            protected void writePersistedField(String name, Vec3 object, NBTTagCompound tag) {
                NBTTagCompound vec = new NBTTagCompound();
                vec.setDouble("x", object.xCoord);
                vec.setDouble("y", object.yCoord);
                vec.setDouble("z", object.zCoord);
                tag.setTag(name, vec);
            }

            @Override
            protected Vec3 readPersistedField(String name, NBTTagCompound tag) {
                NBTTagCompound vec = tag.getCompoundTag(name);
                return new Vec3(vec.getDouble("x"), vec.getDouble("y"), vec.getDouble("z"));
            }

            @Override
            protected Vec3 getDefaultValue() {
                return null;
            }
        });

        NBTYPES.put(Pair.class, new NBTClassType<Pair>() {

            @Override
            protected void writePersistedField(String name, Pair object, NBTTagCompound tag) {
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
            protected Pair readPersistedField(String name, NBTTagCompound tag) {
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
            protected Pair getDefaultValue() {
                return Pair.of(null, null);
            }
        });

        NBTYPES.put(DimPos.class, new NBTClassType<DimPos>() {

            @Override
            protected void writePersistedField(String name, DimPos object, NBTTagCompound tag) {
                NBTTagCompound dimPos = new NBTTagCompound();
                dimPos.setDouble("dim", object.getWorld().provider.getDimensionId());
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
            protected DimPos readPersistedField(String name, NBTTagCompound tag) {
                NBTTagCompound dimPos = tag.getCompoundTag(name);
                int dim = dimPos.getInteger("dim");
                World world;
                if(!MinecraftHelpers.isClientSide()) {
                    if (MinecraftServer.getServer().worldServers.length >= dim) {
                        dim = 0;
                    }
                    world = MinecraftServer.getServer().worldServers[dim];
                } else {
                    world = getClientWorld();
                    if(world.provider.getDimensionId() != dim) {
                        CyclopsCore.clog(Level.WARN, String.format("Tried to fetch dimension %s at client-side while dimension %s was loaded.", dim, world.provider.getDimensionId()));
                    }
                }
                return DimPos.of(world, new BlockPos(dimPos.getInteger("x"), dimPos.getInteger("y"), dimPos.getInteger("z")));
            }

            @Override
            protected DimPos getDefaultValue() {
                return null;
            }
        });
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

    protected static NBTClassType getType(Class<?> type, Object target) {
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
            throw new RuntimeException("Could not access field " + fieldName + " in " + provider.getClass());
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
        Object castTile = field.getDeclaringClass().cast(provider);
        if(write) {
            try {
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
                throw new RuntimeException("Can not write the field " + field.getName() + " in " + castTile + " since it does not exist.");
            }
        } else {
            T object = null;
            try {
                if(tag.hasKey(name)) {
                    object = readPersistedField(name, tag);
                } else {
                    object = getDefaultValue();
                }
                field.set(castTile, object);
            }  catch (IllegalArgumentException e) {
                throw new RuntimeException("Can not read the field " + field.getName() + " as " + object + " in " + castTile + " since it does not exist OR there is a class mismatch.");
            }

        }
    }
    
    protected abstract void writePersistedField(String name, T object, NBTTagCompound tag);
    protected abstract T readPersistedField(String name, NBTTagCompound tag);
    protected abstract T getDefaultValue();

    private abstract static class CollectionNBTClassType<C extends Collection> extends NBTClassType<C> {

        protected abstract C createNewCollection();

        @Override
        protected C getDefaultValue() {
            return createNewCollection();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void writePersistedField(String name, C object, NBTTagCompound tag) {
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
        protected C readPersistedField(String name, NBTTagCompound tag) {
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
