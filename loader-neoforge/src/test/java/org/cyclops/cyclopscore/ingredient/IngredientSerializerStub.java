package org.cyclops.cyclopscore.ingredient;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

public class IngredientSerializerStub<T, M> implements IIngredientSerializer<T, M> {
    @Override
    public Tag serializeInstance(HolderLookup.Provider lookupProvider, T instance) {
        return null;
    }

    @Override
    public T deserializeInstance(HolderLookup.Provider lookupProvider, Tag tag) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Tag serializeCondition(M matchCondition) {
        return null;
    }

    @Override
    public M deserializeCondition(Tag tag) throws IllegalArgumentException {
        return null;
    }
}
