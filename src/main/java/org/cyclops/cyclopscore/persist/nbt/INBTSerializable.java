package org.cyclops.cyclopscore.persist.nbt;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Objects that are serializable to NBT.
 * Classes implementing must have a public default constructor which will be used
 * for NBT reading instantiation.
 * @author rubensworks
 *
 */
public interface INBTSerializable {

	/**
	 * Convert the data to an NBT tag.
	 * @return The NBT tag.
	 */
	public CompoundTag toNBT();
	/**
	 * Read the data from an NBT tag and place it in this object.
     * The given tag will never be null, so make sure that all fields have a correct default value in case
     * the received tag would be null anyways.
	 * @param tag The tag to read from.
	 */
	public void fromNBT(CompoundTag tag);

    @EqualsAndHashCode(callSuper = false)
    @Data
    public static class SelfNBTClassType extends NBTClassType<INBTSerializable> {

        private final Class<?> fieldType;

        @Override
        public void writePersistedField(String name, INBTSerializable object, CompoundTag tag) {
            try {
                Method method = fieldType.getMethod("toNBT");
                tag.put(name, (Tag) method.invoke(object));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No method toNBT for field " + name + " of class " + fieldType + " was found.");
            } catch (InvocationTargetException e) {
                e.getTargetException().printStackTrace();
                throw new RuntimeException("Error in toNBT for field " + name + ". Error: " + e.getTargetException().getMessage());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could invoke toNBT for " + name + ".");
            }

        }

        @Override
        public INBTSerializable readPersistedField(String name, CompoundTag tag) {
            try {
                Constructor<?> constructor = fieldType.getConstructor();
                if(constructor == null) {
                    throw new RuntimeException("The NBT serializable " + name + " of class " + fieldType + " must " +
                            "have a constructor without parameters.");
                }
                Method method = fieldType.getMethod("fromNBT", CompoundTag.class);
                INBTSerializable obj = (INBTSerializable) constructor.newInstance();
                if(tag.contains(name)) {
                    method.invoke(obj, tag.get(name));
                } else {
                    CyclopsCore.clog(Level.WARN, String.format("The tag %s did not contain the key %s, skipping " +
                            "reading.", tag, name));
                }
                return obj;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No method fromNBT for field " + name + " of class " + fieldType + " was found.");
            } catch (InvocationTargetException e) {
                e.getTargetException().printStackTrace();
                throw new RuntimeException("Error in fromNBT for field " + name + ". Error: " + e.getTargetException().getMessage());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could invoke fromNBT for " + name + ".");
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException("Something went wrong while calling the empty constructor for " + name
                        + "of class " + fieldType + ".");
            }
        }

        @Override
        public INBTSerializable getDefaultValue() {
            return null;
        }
    }
	
}
