package org.cyclops.cyclopscore.ingredient;

import net.minecraft.nbt.NBTBase;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

public class IngredientSerializerStub<T, M> implements IIngredientSerializer<T, M> {
    @Override
    public NBTBase serializeInstance(T instance) {
        return null;
    }

    @Override
    public T deserializeInstance(NBTBase tag) throws IllegalArgumentException {
        return null;
    }

    @Override
    public NBTBase serializeCondition(M matchCondition) {
        return null;
    }

    @Override
    public M deserializeCondition(NBTBase tag) throws IllegalArgumentException {
        return null;
    }
}
