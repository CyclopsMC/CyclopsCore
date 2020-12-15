package org.cyclops.cyclopscore.capability.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * A fluid handler that is mutable.
 * @author rubensworks
 */
public interface IFluidHandlerMutable extends IFluidHandler {

    public void setFluidInTank(int tank, FluidStack fluidStack);

}
