package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.FluidHelpers;

/**
 * Slots that will accept buckets and fluid containers.
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
        if (!itemStack.isEmpty()) {
            itemStack = itemStack.copy();
            return FluidUtil.getFluidHandler(itemStack.split(1))
                    .map((fluidHandler) -> tank.fill(FluidHelpers.getFluid(fluidHandler), IFluidHandler.FluidAction.SIMULATE) > 0)
                    .orElse(false);
        }
        return false;
    }
    
}
