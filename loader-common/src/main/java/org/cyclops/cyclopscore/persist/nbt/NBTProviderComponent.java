package org.cyclops.cyclopscore.persist.nbt;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Default implementation of {@link org.cyclops.cyclopscore.persist.nbt.INBTProvider} as a component.
 * @author rubensworks
 */
public class NBTProviderComponent implements INBTProvider {

    private final INBTProvider provider;

    private List<Field> nbtPersistedFields = null;

    public NBTProviderComponent(INBTProvider provider) {
        this.provider = provider;
        generateNBTPersistedFields();
    }

    private void generateNBTPersistedFields() {
        nbtPersistedFields = new LinkedList<Field>();
        for(Class<?> clazz = provider.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            for(Field field : clazz.getDeclaredFields()) {
                if(field.isAnnotationPresent(NBTPersist.class)) {
                    nbtPersistedFields.add(field);
                }
            }
        }
    }

    private void writePersistedField(Field field, ValueOutput output) {
        NBTClassType.performActionForField(provider, field, Either.right(output));
    }

    private void readPersistedField(Field field, ValueInput input) {
        NBTClassType.performActionForField(provider, field, Either.left(input));
    }

    @Override
    public void writeGeneratedFieldsToNBT(ValueOutput output) {
        for(Field field : nbtPersistedFields) {
            writePersistedField(field, output);
        }
    }

    @Override
    public void readGeneratedFieldsFromNBT(ValueInput input) {
        for(Field field : nbtPersistedFields) {
            readPersistedField(field, input);
        }
    }
}
