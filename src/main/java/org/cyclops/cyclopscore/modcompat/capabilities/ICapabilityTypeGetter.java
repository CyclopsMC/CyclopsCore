package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraftforge.common.capabilities.Capability;

/**
 * Getter for capability types.
 * @param <C> The capability type
 * @author rubensworks
 */
public interface ICapabilityTypeGetter<C> {

    /**
     * @return A reference to the capability.
     */
    public Capability<C> getCapability();

}
