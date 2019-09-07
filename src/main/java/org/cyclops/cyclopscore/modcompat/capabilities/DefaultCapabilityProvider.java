package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

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
        return Objects.requireNonNull(capabilityGetter.getCapability(), "A registered capability is null");
    }

    @Override
    public <T2> LazyOptional<T2> getCapability(Capability<T2> capability, Direction facing) {
        if(this.getCapabilityType() == Objects.requireNonNull(capability, "A given capability is null")) {
            return LazyOptional.of(() -> this.capability).cast();
        }
        return LazyOptional.empty();
    }
}
