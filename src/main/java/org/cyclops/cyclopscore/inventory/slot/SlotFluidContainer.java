package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.cyclops.cyclopscore.fluid.SingleUseTank;

/**
 * Slots that will accept buckets and {@link IFluidContainerItem}.
 * @author rubensworks
 *
 */
public class SlotFluidContainer extends Slot {
    
    private SingleUseTank tank = null;
    
    /**
     * Make a new instance that accepts containers for all fluids.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public SlotFluidContainer(IInventory inventory, int index, int x,
                              int y) {
        super(inventory, index, x, y);
    }
    
    /**
     * Make a new instance that accepts containers for a given fluid.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param tank The accepting tank.
     */
    public SlotFluidContainer(IInventory inventory, int index, int x,
                              int y, SingleUseTank tank) {
        this(inventory, index, x, y);
        this.tank = tank;
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return checkIsItemValid(itemStack, tank);
    }
    
    /**
     * Check if the given item is valid and the fluid equals the fluid inside the
     * container (or the fluid in the container is null).
     * @param itemStack The item that will be checked.
     * @param tank The accepting tank.
     * @return If the given item is valid.
     */
    public static boolean checkIsItemValid(ItemStack itemStack, SingleUseTank tank) {
        if (itemStack != null) {
            itemStack = itemStack.copy();
            ItemStack result = FluidUtil.tryEmptyContainer(itemStack.splitStack(1), tank, Integer.MAX_VALUE, null, false);
            if (result != null) {
                if (result.stackSize == 0 || itemStack.stackSize == 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
}
