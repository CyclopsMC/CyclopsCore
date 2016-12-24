package org.cyclops.cyclopscore.capability.fluid;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nullable;

/**
 * An itemfluid handler with a mutable capacity.
 * @author rubensworks
 */
public class FluidHandlerItemCapacity extends FluidHandlerItemStack implements IFluidHandlerItemCapacity {

    /**
     * @param container The container itemStack, data is stored on it directly as NBT.
     * @param capacity  The maximum capacity of this fluid tank.
     */
    public FluidHandlerItemCapacity(ItemStack container, int capacity) {
        super(container, capacity);
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == FluidHandlerItemCapacityConfig.CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == FluidHandlerItemCapacityConfig.CAPABILITY ? (T) this : super.getCapability(capability, facing);
    }

    public static class Storage implements Capability.IStorage<FluidHandlerItemCapacity> {

        @Override
        public NBTBase writeNBT(Capability<FluidHandlerItemCapacity> capability, FluidHandlerItemCapacity instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            FluidStack fluid = instance.getFluid();
            if (fluid != null) {
                fluid.writeToNBT(nbt);
            } else {
                nbt.setString("Empty", "");
            }
            nbt.setInteger("Capacity", instance.getCapacity());
            return nbt;
        }

        @Override
        public void readNBT(Capability<FluidHandlerItemCapacity> capability, FluidHandlerItemCapacity instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound) nbt;
            instance.setCapacity(tags.getInteger("Capacity"));
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tags);
            instance.setFluid(fluid);
        }
    }
}
