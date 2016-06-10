package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * Constructor for capabilities.
 * @param <C> The capability type
 * @param <H> The host that will contain the capability.
 * @author rubensworks
 */
public interface ICapabilityConstructor<C, H> {

    /**
     * @return A reference to the capability.
     */
    public Capability<C> getCapability();

    /**
     * @param host The host for capabilities
     * @return A new capability provider for the given host.
     */
    public @Nullable ICapabilityProvider createProvider(H host);

}
