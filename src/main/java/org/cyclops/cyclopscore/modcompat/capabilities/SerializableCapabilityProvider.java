package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * A serializable implementation of the capability provider.
 * @author rubensworks
 */
public abstract class SerializableCapabilityProvider<T> extends DefaultCapabilityProvider<T> implements INBTSerializable<Tag> {

    public SerializableCapabilityProvider(ICapabilityTypeGetter<T> capabilityGetter, T capability) {
        super(capabilityGetter, capability);
    }

    @Deprecated
    public SerializableCapabilityProvider(Capability<T> capabilityType, T capability) {
        super(capabilityType, capability);
    }

    @Override
    public Tag serializeNBT() {
        return serializeNBT(capability.orElse(null));
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        deserializeNBT(capability.orElse(null), nbt);
    }

    protected abstract Tag serializeNBT(T capability);

    protected abstract void deserializeNBT(T capability, Tag tag);
}
