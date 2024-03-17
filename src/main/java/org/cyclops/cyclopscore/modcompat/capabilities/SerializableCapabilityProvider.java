package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * A serializable implementation of the capability provider.
 * @author rubensworks
 */
public abstract class SerializableCapabilityProvider<O, C, T> extends DefaultCapabilityProvider<O, C, T> implements INBTSerializable<Tag> {

    public SerializableCapabilityProvider(T capability) {
        super(capability);
    }

    @Override
    public Tag serializeNBT() {
        return serializeNBT(capability);
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        deserializeNBT(capability, nbt);
    }

    protected abstract Tag serializeNBT(T capability);

    protected abstract void deserializeNBT(T capability, Tag tag);
}
