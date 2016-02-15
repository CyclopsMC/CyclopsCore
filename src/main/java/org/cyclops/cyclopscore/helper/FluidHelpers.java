package org.cyclops.cyclopscore.helper;

import net.minecraftforge.fluids.FluidStack;

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

}
