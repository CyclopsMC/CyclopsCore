package org.cyclops.cyclopscore.capability.fluid;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nullable;

/**
 * An itemfluid handler with a mutable capacity.
 * @author rubensworks
 */
public class FluidHandlerItemCapacity extends FluidHandlerItemStack implements IFluidHandlerItemCapacity, IFluidHandlerMutable, INBTSerializable {

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
        super(container, capacity);
        this.fluid = fluid;
    }

    @Override
    public boolean canFillFluidType(FluidStack resource) {
        return fluid == null || resource == null || fluid == resource.getFluid();
    }

    @Override
    public void setCapacity(int capacity) {
        CompoundTag tag = getContainer().getOrCreateTag();
        this.capacity = capacity;
        tag.putInt("capacity", capacity);
    }

    @Override
    public int getCapacity() {
        CompoundTag tag = getContainer().getOrCreateTag();
        return tag.contains("capacity", Tag.TAG_INT) ? tag.getInt("capacity") : this.capacity;
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        this.capacity = getCapacity(); // Force overriding protected capacity field as soon as possible.
        return super.getFluid();
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        return capability == FluidHandlerItemCapacityConfig._instance.getCapability() ? LazyOptional.of(() -> this).cast() : super.getCapability(capability, facing);
    }

    @Override
    public void setFluidInTank(int tank, FluidStack fluidStack) {
        if (tank == 0) {
            setFluid(fluidStack);
        }
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        FluidStack fluid = this.getFluid();
        if (fluid != null) {
            fluid.writeToNBT(nbt);
        } else {
            nbt.putString("Empty", "");
        }
        nbt.putInt("capacity", this.getCapacity());
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CompoundTag tags = (CompoundTag) nbt;
        if (tags.contains("capacity", Tag.TAG_INT)) {
            this.setCapacity(tags.getInt("capacity"));
        }
        FluidStack fluid = FluidStack.loadFluidStackFromNBT(tags);
        this.setFluid(fluid);
    }
}
