package org.cyclops.cyclopscore.modcompat;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * Compatibility for external mod capabilities.
 * It is safe to do anything with the target capability, since this will
 * only be loaded if the target capability is available.
 * @param <P> The type of capability provider.
 * @author rubensworks
 */
public interface ICapabilityCompat<P extends ICapabilityProvider> {

    public void attach(P provider);

    /**
     * Reference to a capability instance.
     * @param <C> Tne type of capability.
     */
    public static interface ICapabilityReference<C> {
        /**
         * @return A capability, this will probably refer to a field annotated with
         *         {@link net.minecraftforge.common.capabilities.CapabilityInject}.
         */
        public Capability<C> getCapability();
    }

}
