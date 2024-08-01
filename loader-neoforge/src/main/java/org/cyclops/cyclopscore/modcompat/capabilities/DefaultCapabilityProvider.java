package org.cyclops.cyclopscore.modcompat.capabilities;

import net.neoforged.neoforge.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A default implementation of the capability provider.
 * @author rubensworks
 */
public class DefaultCapabilityProvider<O, C, T> implements ICapabilityProvider<O, C, T> {

    protected final T capability;

    public DefaultCapabilityProvider(T capability) {
        this.capability = Objects.requireNonNull(capability);
    }

    @Nullable
    @Override
    public T getCapability(O object, C context) {
        return this.capability;
    }
}
