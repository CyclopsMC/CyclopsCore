package org.cyclops.cyclopscore.capability.fluid;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import org.cyclops.cyclopscore.RegistryEntries;

import javax.annotation.Nullable;

/**
 * An itemfluid handler with a mutable capacity.
 * @author rubensworks
 */
public class FluidHandlerItemCapacity extends FluidHandlerItemStack implements IFluidHandlerItemCapacity, IFluidHandlerMutable {

    private final Fluid fluid;

    /**
     * @param container The container itemStack, data is stored on it directly as NBT.
     * @param capacity  The maximum capacity of this fluid tank.
     */
    public FluidHandlerItemCapacity(ItemStack container, int capacity) {
        this(container, capacity, null);
    }

    /**
     * @param container The container itemStack, data is stored on it directly as NBT.
     * @param capacity  The maximum capacity of this fluid tank.
     * @param fluid     The accepted fluid.
     */
    public FluidHandlerItemCapacity(ItemStack container, int capacity, Fluid fluid) {
        super(RegistryEntries.COMPONENT_FLUID_CONTENT, container, capacity);
        this.fluid = fluid;
    }

    @Override
    public boolean canFillFluidType(FluidStack resource) {
        return fluid == null || resource == null || fluid == resource.getFluid();
    }

    @Override
    public void setCapacity(int capacity) {
        getContainer().set(RegistryEntries.COMPONENT_CAPACITY, capacity);
        this.capacity = capacity;
    }

    @Override
    public int getCapacity() {
        return getContainer().has(RegistryEntries.COMPONENT_CAPACITY) ? getContainer().get(RegistryEntries.COMPONENT_CAPACITY) : this.capacity;
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        this.capacity = getCapacity(); // Force overriding protected capacity field as soon as possible.
        return super.getFluid();
    }

    @Override
    public void setFluidInTank(int tank, FluidStack fluidStack) {
        if (tank == 0) {
            setFluid(fluidStack);
        }
    }
}
