package org.cyclops.cyclopscore.capability.fluid;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * A fluid handler with a mutable capacity.
 * @author rubensworks
 */
public interface IFluidHandlerCapacity extends IFluidHandler {

    public void setTankCapacity(int tank, int capacity);
    public int getTankCapacity(int tank);

}
