package org.cyclops.cyclopscore.fluid;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
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
    public CompoundTag writeToNBT(CompoundTag nbt) {
        if(replaceInnerFluid()) {
            super.writeToNBT(nbt);
        }
        writeTankToNBT(nbt);
        return nbt;
    }

    public CompoundTag writeToNBT(CompoundTag nbt, String tag) {
        CompoundTag subTag = new CompoundTag();
        writeToNBT(subTag);
        nbt.put(tag, subTag);
        return nbt;
    }

    @Override
    public FluidTank readFromNBT(CompoundTag nbt) {
        if(replaceInnerFluid()) {
            if (nbt.contains("Empty")) {
                setFluid(null);
            }
            super.readFromNBT(nbt);
        }
        readTankFromNBT(nbt);
        return this;
    }

    public FluidTank readFromNBT(CompoundTag data, String tag) {
        CompoundTag subTag = data.getCompound(tag);
        return readFromNBT(subTag);
    }

    /**
     * Write the tank contents to NBT.
     * @param nbt The NBT tag to write to.
     */
    public void writeTankToNBT(CompoundTag nbt) {
        nbt.putInt("capacity", getCapacity());
    }

    /**
     * Read the tank contents from NBT.
     * @param nbt The NBT tag to write from.
     */
    public void readTankFromNBT(CompoundTag nbt) {
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
