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

    private final Capability<T> capabilityType;
    private final T capability;

    public DefaultCapabilityProvider(Capability<T> capabilityType, T capability) {
        this.capabilityType = Objects.requireNonNull(capabilityType);
        this.capability = Objects.requireNonNull(capability);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return this.capabilityType == capability;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(hasCapability(capability, facing)) {
            return (T) this.capability;
        }
        return null;
    }
}
