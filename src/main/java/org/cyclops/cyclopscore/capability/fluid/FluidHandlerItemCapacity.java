package org.cyclops.cyclopscore.capability.fluid;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

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
     * @param fluid
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
        NBTTagCompound tag = ItemStackHelpers.getSafeTagCompound(getContainer());
        this.capacity = capacity;
        tag.setInteger("capacity", capacity);
    }

    @Override
    public int getCapacity() {
        NBTTagCompound tag = ItemStackHelpers.getSafeTagCompound(getContainer());
        return tag.hasKey("capacity", MinecraftHelpers.NBTTag_Types.NBTTagInt.ordinal()) ? tag.getInteger("capacity") : this.capacity;
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        this.capacity = getCapacity(); // Force overriding protected capacity field as soon as possible.
        return super.getFluid();
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
            nbt.setInteger("capacity", instance.getCapacity());
            return nbt;
        }

        @Override
        public void readNBT(Capability<FluidHandlerItemCapacity> capability, FluidHandlerItemCapacity instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound) nbt;
            if (tags.hasKey("capacity", MinecraftHelpers.NBTTag_Types.NBTTagInt.ordinal())) {
                instance.setCapacity(tags.getInteger("capacity"));
            }
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tags);
            instance.setFluid(fluid);
        }
    }
}
