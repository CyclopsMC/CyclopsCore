package org.cyclops.cyclopscore.ingredient;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

public class IngredientSerializerInt implements IIngredientSerializer<Integer, Boolean> {

    @Override
    public Tag serializeInstance(Integer instance) {
        return IntTag.valueOf(instance);
    }

    @Override
    public Integer deserializeInstance(Tag tag) throws IllegalArgumentException {
        if (!(tag instanceof IntTag)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagInt");
        }
        return ((IntTag) tag).getAsInt();
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
        return ((ByteTag) tag).getAsByte() == 1;
    }

}
