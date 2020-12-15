package org.cyclops.cyclopscore.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerMutable;

/**
 * A simple fluid tank.
 * Based on the Buildcraft Tank
 * @author rubensworks
 *
 */
public class Tank extends FluidTank implements IFluidHandlerCapacity, IFluidHandlerMutable {

    /**
     * Make a new fluid tank.
     * @param capacity The capacity (mB) for the tank.
     */
    public Tank(int capacity) {
        super(capacity);
    }
    
    /**
     * Check if this tank is empty.
     * @return If the tank is empty; no fluid is inside of it.
     */
    public boolean isEmpty() {
        return getFluid().isEmpty() || getFluid().getAmount() <= 0;
    }

    /**
     * Check if this tank is full; the capacity is reached.
     * @return If this tank is full.
     */
    public boolean isFull() {
        return !getFluid().isEmpty() && getFluid().getAmount() >= getCapacity();
    }

    /**
     * Get the fluid that currently occupies this tank, will return null if there is no fluid.
     * @return The inner fluid.
     */
    public Fluid getFluidType() {
        return !getFluid().isEmpty() ? getFluid().getFluid() : null;
    }

    protected boolean replaceInnerFluid() {
        return true;
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        if(replaceInnerFluid()) {
            super.writeToNBT(nbt);
        }
        writeTankToNBT(nbt);
        return nbt;
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt, String tag) {
        CompoundNBT subTag = new CompoundNBT();
        writeToNBT(subTag);
        nbt.put(tag, subTag);
        return nbt;
    }

    @Override
    public FluidTank readFromNBT(CompoundNBT nbt) {
        if(replaceInnerFluid()) {
            if (nbt.contains("Empty")) {
                setFluid(null);
            }
            super.readFromNBT(nbt);
        }
        readTankFromNBT(nbt);
        return this;
    }

    public FluidTank readFromNBT(CompoundNBT data, String tag) {
        CompoundNBT subTag = data.getCompound(tag);
        return readFromNBT(subTag);
    }

    /**
     * Write the tank contents to NBT.
     * @param nbt The NBT tag to write to.
     */
    public void writeTankToNBT(CompoundNBT nbt) {
    	nbt.putInt("capacity", getCapacity());
    }

    /**
     * Read the tank contents from NBT.
     * @param nbt The NBT tag to write from.
     */
    public void readTankFromNBT(CompoundNBT nbt) {
    	if(nbt.contains("capacity")) { // Backwards compatibility.
    		setCapacity(nbt.getInt("capacity"));
    	}
    }

    @Override
    public void setFluidInTank(int tank, FluidStack fluidStack) {
        if (tank == 0) {
            setFluid(fluidStack);
        }
    }

    @Override
    public void setTankCapacity(int tank, int capacity) {
        if (tank == 0) {
            setCapacity(capacity);
        }
    }

    @Override
    public int getTankCapacity(int tank) {
        if (tank == 0) {
            return getCapacity();
        }
        return 0;
    }
}
