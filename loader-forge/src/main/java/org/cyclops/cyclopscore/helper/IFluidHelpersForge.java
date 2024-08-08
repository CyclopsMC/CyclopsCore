package org.cyclops.cyclopscore.helper;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public interface IFluidHelpersForge {

    public int getBucketVolume();

    /**
     * Get the fluid amount of the given stack in a safe manner.
     * @param fluidStack The fluid stack
     * @return The fluid amount.
     */
    public int getAmount(FluidStack fluidStack);

    /**
     * Copy the given fluid stack
     * @param fluidStack The fluid stack to copy.
     * @return A copy of the fluid stack.
     */
    public FluidStack copy(FluidStack fluidStack);

    /**
     * If this destination can completely contain the given fluid in the given source.
     * @param source The source of the fluid that has to be moved.
     * @param destination The target of the fluid that has to be moved.
     * @return If the destination can completely contain the fluid of the source.
     */
    public boolean canCompletelyFill(IFluidHandler source, IFluidHandler destination);

    /**
     * Get the fluid contained in a fluid handler.
     * @param fluidHandler The fluid handler.
     * @return The fluid.
     */
    public FluidStack getFluid(@Nullable IFluidHandler fluidHandler);

    /**
     * Check if the fluid handler is not empty.
     * @param fluidHandler The fluid handler.
     * @return If it is not empty.
     */
    public boolean hasFluid(@Nullable IFluidHandler fluidHandler);

    /**
     * Get the capacity of a fluid handler.
     * @param fluidHandler The fluid handler.
     * @return The capacity.
     */
    public int getCapacity(@Nullable IFluidHandler fluidHandler);

    /**
     * Convert a boolean-based simulate to a fluid action enum value.
     * @param simulate If in simulation mode.
     * @return The fluid action.
     */
    public IFluidHandler.FluidAction simulateBooleanToAction(boolean simulate);

}
