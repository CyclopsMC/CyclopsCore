package org.cyclops.cyclopscore.persist.nbt;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

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

    private void writePersistedField(Field field, CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        NBTClassType.performActionForField(provider, field, tag, true, holderLookupProvider);
    }

    private void readPersistedField(Field field, CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        NBTClassType.performActionForField(provider, field, tag, false, holderLookupProvider);
    }

    @Override
    public void writeGeneratedFieldsToNBT(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        for(Field field : nbtPersistedFields) {
            writePersistedField(field, tag, holderLookupProvider);
        }
    }

    @Override
    public void readGeneratedFieldsFromNBT(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        for(Field field : nbtPersistedFields) {
            readPersistedField(field, tag, holderLookupProvider);
        }
    }
}
