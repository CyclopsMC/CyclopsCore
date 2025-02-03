package org.cyclops.cyclopscore.capability.fluid;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import org.cyclops.cyclopscore.RegistryEntries;

import javax.annotation.Nullable;

/**
 * An itemfluid handler with a mutable capacity.
 * @author rubensworks
 */
public class FluidHandlerItemCapacity extends FluidHandlerItemStack implements IFluidHandlerItemCapacity, IFluidHandlerMutable {

    private final Fluid fluid;
    private final int capacityDefault;

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
        this.capacityDefault = capacity;
    }

    @Override
    public boolean canFillFluidType(FluidStack resource) {
        return fluid == null || resource == null || fluid == resource.getFluid();
    }

    @Override
    protected void setFluid(FluidStack fluid) {
        // super.setFluid(fluid); // We override the implementation completely to avoid NBT saving for empty fluids

        if (fluid.isEmpty()) {
            this.container.remove(this.componentType);
        } else {
            this.container.set(this.componentType, SimpleFluidContent.copyOf(fluid));
        }
    }

    @Override
    public void setCapacity(int capacity) {
        if (capacity == this.capacityDefault) {
            getContainer().remove(RegistryEntries.COMPONENT_CAPACITY);
        } else {
            getContainer().set(RegistryEntries.COMPONENT_CAPACITY, capacity);
        }
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
