package org.cyclops.cyclopscore.fluid;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;

/**
 * A simple fluid tank.
 * Based on the Buildcraft Tank
 * @author rubensworks
 *
 */
public class Tank extends FluidStacksResourceHandler implements IFluidHandlerCapacity {

    /**
     * Make a new fluid tank.
     * @param capacity The capacity (mB) for the tank.
     */
    public Tank(int capacity) {
        super(1, capacity);
    }

    public FluidStack getFluid() {
        return size() == 0 ? FluidStack.EMPTY : this.stacks.get(0);
    }

    public int getFluidAmount() {
        return getFluid().getAmount();
    }

    public void setFluid(FluidStack fluid) {
        this.stacks.set(0, fluid);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
    public void serialize(ValueOutput output) {
        if(replaceInnerFluid()) {
            super.serialize(output);
        }
        serializeTank(output);
    }

    public void serialize(ValueOutput output, String tag) {
        serialize(output.child(tag));
    }

    @Override
    public void deserialize(ValueInput input) {
        if(replaceInnerFluid()) {
            if (input.child("Empty").isPresent()) {
                setFluid(null);
            }
            super.deserialize(input);
        }
        deserializeTank(input);
    }

    public void deserialize(ValueInput input, String tag) {
        deserialize(input.child(tag).orElseThrow());
    }

    /**
     * Write the tank contents to NBT.
     * @param output The value tag to write to.
     */
    public void serializeTank(ValueOutput output) {
        output.putInt("capacity", getCapacity());
    }

    /**
     * Read the tank contents from NBT.
     * @param input The value to write from.
     */
    public void deserializeTank(ValueInput input) {
        setCapacity(input.getInt("capacity").orElseThrow());
    }

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
