package org.cyclops.cyclopscore.persist.nbt;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
     * Convert the data to an output.
     * @param valueOutput the output.
     */
    public void toValueOutput(ValueOutput valueOutput);
    /**
     * Read the data from an input and place it in this object.
     * @param valueInput The input.
     */
    public void fromValueInput(ValueInput valueInput);

    public static class SelfNBTClassType extends NBTClassType<INBTSerializable> {

        private final Class<?> fieldType;

        public SelfNBTClassType(Class<?> fieldType) {
            this.fieldType = fieldType;
        }

        public Class<?> getFieldType() {
            return fieldType;
        }

        @Override
        public void writePersistedField(String name, INBTSerializable object, ValueOutput tag) {
            try {
                Method method = fieldType.getMethod("toValueOutput", ValueOutput.class);
                method.invoke(object, tag);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No method toValueOutput for field " + name + " of class " + fieldType + " was found.");
            } catch (InvocationTargetException e) {
                e.getTargetException().printStackTrace();
                throw new RuntimeException("Error in toValueOutput for field " + name + ". Error: " + e.getTargetException().getMessage());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could invoke toValueOutput for " + name + ".");
            }

        }

        @Override
        public INBTSerializable readPersistedField(String name, ValueInput tag) {
            try {
                Constructor<?> constructor = fieldType.getConstructor();
                if(constructor == null) {
                    throw new RuntimeException("The NBT serializable " + name + " of class " + fieldType + " must " +
                            "have a constructor without parameters.");
                }
                Method method = fieldType.getMethod("fromValueInput", ValueInput.class);
                INBTSerializable obj = (INBTSerializable) constructor.newInstance();
                tag.child(name).ifPresentOrElse(
                        child -> {
                            try {
                                method.invoke(obj, child);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("Could invoke fromValueInput for " + name + ".");
                            } catch (InvocationTargetException e) {
                                e.getTargetException().printStackTrace();
                                throw new RuntimeException("Error in fromValueInput for field " + name + ". Error: " + e.getTargetException().getMessage());
                            }
                        },
                        () -> {
                            System.out.println(String.format("The tag %s did not contain the key %s, skipping " +
                                    "reading.", tag, name));
                        }
                );
                return obj;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No method fromValueInput for field " + name + " of class " + fieldType + " was found.");
            } catch (InvocationTargetException e) {
                e.getTargetException().printStackTrace();
                throw new RuntimeException("Error in fromValueInput for field " + name + ". Error: " + e.getTargetException().getMessage());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could invoke fromValueInput for " + name + ".");
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
