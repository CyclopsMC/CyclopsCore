package org.cyclops.cyclopscore.helper;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

/**
 * Contains helper methods for various fluid specific things.
 * @author rubensworks
 */
public final class FluidHelpers {

    /**
     * Get the fluid amount of the given stack in a safe manner.
     * @param fluidStack The fluid stack
     * @return The fluid amount.
     */
    public static int getAmount(@Nullable FluidStack fluidStack) {
        return fluidStack != null ? fluidStack.amount : 0;
    }

    /**
     * Copy the given fluid stack
     * @param fluidStack The fluid stack to copy.
     * @return A copy of the fluid stack.
     */
    public static FluidStack copy(@Nullable FluidStack fluidStack) {
        if(fluidStack == null) return null;
        return new FluidStack(fluidStack.getFluid(), fluidStack.amount, fluidStack.tag);
    }

    /**
     * If this destination can completely contain the given fluid in the given source.
     * @param source The source of the fluid that has to be moved.
     * @param destination The target of the fluid that has to be moved.
     * @return If the destination can completely contain the fluid of the source.
     */
    public static boolean canCompletelyFill(IFluidHandler source, IFluidHandler destination) {
        FluidStack drained = source.drain(Integer.MAX_VALUE, false);
        return drained != null && destination.fill(drained, false) == drained.amount;
    }

}
