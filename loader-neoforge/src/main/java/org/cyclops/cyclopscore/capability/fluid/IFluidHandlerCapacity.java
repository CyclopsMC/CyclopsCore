package org.cyclops.cyclopscore.capability.fluid;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * A fluid handler with a mutable capacity.
 * @author rubensworks
 */
public interface IFluidHandlerCapacity extends ResourceHandler<FluidResource> {

    public void setTankCapacity(int tank, int capacity, TransactionContext transaction);

    public int getTankCapacity(int tank);

}
