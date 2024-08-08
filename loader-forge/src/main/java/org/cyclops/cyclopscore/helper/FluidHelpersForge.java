package org.cyclops.cyclopscore.helper;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

/**
 * @author rubensworks
 */
public class FluidHelpersForge implements IFluidHelpersForge {
    @Override
    public int getBucketVolume() {
        return 1000;
    }

    @Override
    public int getAmount(FluidStack fluidStack) {
        return fluidStack.getAmount();
    }

    @Override
    public FluidStack copy(FluidStack fluidStack) {
        if(fluidStack.isEmpty()) return FluidStack.EMPTY;
        return fluidStack.copy();
    }

    @Override
    public boolean canCompletelyFill(IFluidHandler source, IFluidHandler destination) {
        FluidStack drained = source.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        return !drained.isEmpty() && destination.fill(drained, IFluidHandler.FluidAction.SIMULATE) == drained.getAmount();
    }

    @Override
    public FluidStack getFluid(@Nullable IFluidHandler fluidHandler) {
        return fluidHandler != null ? fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE) : FluidStack.EMPTY;
    }

    @Override
    public boolean hasFluid(@Nullable IFluidHandler fluidHandler) {
        return !getFluid(fluidHandler).isEmpty();
    }

    @Override
    public int getCapacity(@Nullable IFluidHandler fluidHandler) {
        int capacity = 0;
        if (fluidHandler != null) {
            for (int i = 0; i < fluidHandler.getTanks(); i++) {
                capacity += fluidHandler.getTankCapacity(i);
            }
        }
        return capacity;
    }

    @Override
    public IFluidHandler.FluidAction simulateBooleanToAction(boolean simulate) {
        return simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
    }
}
