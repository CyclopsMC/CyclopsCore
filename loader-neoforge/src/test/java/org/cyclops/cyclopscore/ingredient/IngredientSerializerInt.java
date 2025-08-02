package org.cyclops.cyclopscore.ingredient;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

public class IngredientSerializerInt implements IIngredientSerializer<Integer, Boolean> {
    @Override
    public void serializeInstance(ValueOutput valueOutput, Integer instance) {
        valueOutput.putInt("i", instance);
    }

    @Override
    public Integer deserializeInstance(ValueInput valueInput) throws IllegalArgumentException {
        return valueInput.getInt("i").orElseThrow();
    }

    @Override
    public Tag serializeCondition(Boolean matchCondition) {
        return ByteTag.valueOf((byte) (matchCondition ? 1 : 0));
    }

    @Override
    public Boolean deserializeCondition(Tag tag) throws IllegalArgumentException {
        if (!(tag instanceof ByteTag)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagByte");
        }
        return ((ByteTag) tag).byteValue() == (byte) 1;
    }
}
