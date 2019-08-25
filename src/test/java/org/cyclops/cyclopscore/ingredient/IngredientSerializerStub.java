package org.cyclops.cyclopscore.ingredient;

import net.minecraft.nbt.INBT;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

public class IngredientSerializerStub<T, M> implements IIngredientSerializer<T, M> {
    @Override
    public INBT serializeInstance(T instance) {
        return null;
    }

    @Override
    public T deserializeInstance(INBT tag) throws IllegalArgumentException {
        return null;
    }

    @Override
    public INBT serializeCondition(M matchCondition) {
        return null;
    }

    @Override
    public M deserializeCondition(INBT tag) throws IllegalArgumentException {
        return null;
    }
}
