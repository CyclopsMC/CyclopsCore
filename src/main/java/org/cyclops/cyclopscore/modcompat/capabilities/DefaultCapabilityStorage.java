package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

/**
 * A default capability storage implementation when no NBT persistence is required.
 * @author rubensworks
 */
public class DefaultCapabilityStorage<T> implements Capability.IStorage<T> {
    @Override
    public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
        return null;
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {

    }
}
