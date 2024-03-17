package org.cyclops.cyclopscore.modcompat.capabilities;

import net.neoforged.neoforge.capabilities.BaseCapability;

/**
 * Getter for capability types.
 * @param <C> The capability type
 * @author rubensworks
 */
public interface ICapabilityTypeGetter<T, C> {

    /**
     * @return A reference to the capability.
     */
    public BaseCapability<T, C> getCapability();

}
