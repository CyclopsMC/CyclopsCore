package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * Constructor for capabilities when the host and the host type are equal.
 * @param <C> The capability type
 * @param <H> The host that will contain the capability.
 * @author rubensworks
 */
public abstract class SimpleCapabilityConstructor<C, H> implements ICapabilityConstructor<C, H, H> {

    @Nullable
    @Override
    public ICapabilityProvider createProvider(H hostType, H host) {
        return createProvider(host);
    }

    /**
     * @param host The host for capabilities
     * @return A new capability provider for the given host.
     */
    public abstract @Nullable ICapabilityProvider createProvider(H host);

}
