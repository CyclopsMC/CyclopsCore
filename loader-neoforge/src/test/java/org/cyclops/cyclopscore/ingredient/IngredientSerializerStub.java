package org.cyclops.cyclopscore.ingredient;

import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

public class IngredientSerializerStub<T, M> implements IIngredientSerializer<T, M> {
    @Override
    public void serializeInstance(ValueOutput valueOutput, T instance) {

    }

    @Override
    public T deserializeInstance(ValueInput valueInput) throws IllegalArgumentException {
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
