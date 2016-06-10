package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * A default capability storage implementation when no NBT persistence is required.
 * @author rubensworks
 */
public class DefaultCapabilityStorage<T> implements Capability.IStorage<T> {
    @Override
    public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
        return null;
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {

    }
}
