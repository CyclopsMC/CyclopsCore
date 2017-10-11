package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * Constructor for capabilities.
 * @param <C> The capability type
 * @param <T> The host type
 * @param <H> The host that will contain the capability.
 * @author rubensworks
 */
public interface ICapabilityConstructor<C, T, H> extends ICapabilityTypeGetter<C> {

    /**
     * @param hostType The host type for capabilities.
     * @param host The host for capabilities
     * @return A new capability provider for the given host.
     */
    public @Nullable ICapabilityProvider createProvider(T hostType, H host);

}
