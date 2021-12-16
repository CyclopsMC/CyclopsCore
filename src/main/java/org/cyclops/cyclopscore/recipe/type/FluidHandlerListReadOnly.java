package org.cyclops.cyclopscore.recipe.type;

import net.minecraft.core.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * A simple {@link IFluidHandler} implementation that only allows fluid reading.
 * @author rubensworks
 */
public class FluidHandlerListReadOnly implements IFluidHandler {

    private final NonNullList<FluidStack> fluidStacks;

    public FluidHandlerListReadOnly(NonNullList<FluidStack> fluidStacks) {
        this.fluidStacks = fluidStacks;
    }

    @Override
    public int getTanks() {
        return fluidStacks.size();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        if (tank >= getTanks()) {
            return FluidStack.EMPTY;
        }
        return fluidStacks.get(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        throw new UnsupportedOperationException("getTankCapacity is not implemented on FluidHandlerList");
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        throw new UnsupportedOperationException("isFluidValid is not implemented on FluidHandlerList");
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        throw new UnsupportedOperationException("fill is not implemented on FluidHandlerList");
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        throw new UnsupportedOperationException("drain is not implemented on FluidHandlerList");
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        throw new UnsupportedOperationException("drain is not implemented on FluidHandlerList");
    }
}
