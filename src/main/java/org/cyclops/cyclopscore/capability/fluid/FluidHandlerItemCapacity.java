package org.cyclops.cyclopscore.capability.fluid;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nullable;

/**
 * An itemfluid handler with a mutable capacity.
 * @author rubensworks
 */
public class FluidHandlerItemCapacity extends FluidHandlerItemStack implements IFluidHandlerItemCapacity {

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
        CompoundNBT tag = getContainer().getOrCreateTag();
        this.capacity = capacity;
        tag.putInt("capacity", capacity);
    }

    @Override
    public int getCapacity() {
        CompoundNBT tag = getContainer().getOrCreateTag();
        return tag.contains("capacity", Constants.NBT.TAG_INT) ? tag.getInt("capacity") : this.capacity;
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
        return capability == FluidHandlerItemCapacityConfig.CAPABILITY ? LazyOptional.of(() -> this).cast() : super.getCapability(capability, facing);
    }

    public static class Storage implements Capability.IStorage<IFluidHandlerItemCapacity> {

        @Override
        public INBT writeNBT(Capability<IFluidHandlerItemCapacity> capability, IFluidHandlerItemCapacity instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            FluidStack fluid = ((FluidHandlerItemCapacity)instance).getFluid();
            if (fluid != null) {
                fluid.writeToNBT(nbt);
            } else {
                nbt.putString("Empty", "");
            }
            nbt.putInt("capacity", instance.getCapacity());
            return nbt;
        }

        @Override
        public void readNBT(Capability<IFluidHandlerItemCapacity> capability, IFluidHandlerItemCapacity instance, Direction side, INBT nbt) {
            CompoundNBT tags = (CompoundNBT) nbt;
            if (tags.contains("capacity", Constants.NBT.TAG_INT)) {
                instance.setCapacity(tags.getInt("capacity"));
            }
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tags);
            ((FluidHandlerItemCapacity)instance).setFluid(fluid);
        }
    }
}
