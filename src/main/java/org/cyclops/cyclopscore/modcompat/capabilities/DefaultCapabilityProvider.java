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

    protected final ICapabilityTypeGetter<T> capabilityGetter;
    protected final T capability;

    public DefaultCapabilityProvider(ICapabilityTypeGetter<T> capabilityGetter, T capability) {
        this.capabilityGetter = Objects.requireNonNull(capabilityGetter);
        this.capability = Objects.requireNonNull(capability);
    }

    @Deprecated
    public DefaultCapabilityProvider(Capability<T> capabilityType, T capability) {
        Objects.requireNonNull(capabilityType,
                "The given capability can not be null, this is probably being called too early during init");
        this.capabilityGetter = () -> capabilityType;
        this.capability = Objects.requireNonNull(capability);
    }

    public Capability<T> getCapabilityType() {
        return capabilityGetter.getCapability();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return this.getCapabilityType() == capability;
    }

    @Override
    public <T2> T2 getCapability(Capability<T2> capability, EnumFacing facing) {
        if(hasCapability(capability, facing)) {
            return (T2) this.capability;
        }
        return null;
    }
}
