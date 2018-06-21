package org.cyclops.cyclopscore.ingredient;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;

public class IngredientSerializerInt implements IIngredientSerializer<Integer, Boolean> {

    @Override
    public NBTBase serializeInstance(Integer instance) {
        return new NBTTagInt(instance);
    }

    @Override
    public Integer deserializeInstance(NBTBase tag) throws IllegalArgumentException {
        if (!(tag instanceof NBTTagInt)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagInt");
        }
        return ((NBTTagInt) tag).getInt();
    }

    @Override
    public NBTBase serializeCondition(Boolean matchCondition) {
        return new NBTTagByte((byte) (matchCondition ? 1 : 0));
    }

    @Override
    public Boolean deserializeCondition(NBTBase tag) throws IllegalArgumentException {
        if (!(tag instanceof NBTTagByte)) {
            throw new IllegalArgumentException("This deserializer only accepts NBTTagByte");
        }
        return ((NBTTagByte) tag).getByte() == 1;
    }

}
