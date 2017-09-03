package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * A serializable implementation of the capability provider.
 * @author rubensworks
 */
public class SerializableCapabilityProvider<T> extends DefaultCapabilityProvider<T> implements INBTSerializable<NBTBase> {

    public SerializableCapabilityProvider(Capability<T> capabilityType, T capability) {
        super(capabilityType, capability);
    }

    @Override
    public NBTBase serializeNBT() {
        return capabilityType.writeNBT(capability, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        capabilityType.readNBT(capability, null, nbt);
    }
}
