package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Objects;

/**
 * A default implementation of the capability provider.
 * @author rubensworks
 */
public class DefaultCapabilityProvider<T> implements ICapabilityProvider {

    protected final Capability<T> capabilityType;
    protected final T capability;

    public DefaultCapabilityProvider(Capability<T> capabilityType, T capability) {
        this.capabilityType = Objects.requireNonNull(capabilityType);
        this.capability = Objects.requireNonNull(capability);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return this.capabilityType == capability;
    }

    @Override
    public <T2> T2 getCapability(Capability<T2> capability, EnumFacing facing) {
        if(hasCapability(capability, facing)) {
            return (T2) this.capability;
        }
        return null;
    }
}
