package org.cyclops.cyclopscore.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * An ItemStack fluid handler with a mutable capacity.
 * @author rubensworks
 */
public class FluidHandlerCapacityItemStack extends FluidHandlerItemStack implements IFluidHandlerItemCapacity {

    protected static final String NBT_CAPACITY = "fluidhandlercap:capacity";

    /**
     * @param container The container itemStack, data is stored on it directly as NBT.
     * @param capacity  The maximum capacity of this fluid tank.
     */
    public FluidHandlerCapacityItemStack(ItemStack container, int capacity) {
        super(container, capacity);
    }

    @Override
    public void setCapacity(int capacity) {
        NBTTagCompound tag = ItemStackHelpers.getSafeTagCompound(container);
        tag.setInteger(NBT_CAPACITY, capacity);
    }

    @Override
    public int getCapacity() {
        NBTTagCompound tag = ItemStackHelpers.getSafeTagCompound(container);
        return tag.hasKey(NBT_CAPACITY, MinecraftHelpers.NBTTag_Types.NBTTagInt.ordinal()) ? tag.getInteger(NBT_CAPACITY) : this.capacity;
    }
}
