package org.cyclops.cyclopscore.inventory.slot;

import lombok.Getter;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Slot that is used for only accepting one item.
 * @author rubensworks
 *
 */
public class SlotSingleItem extends SlotExtended {

	@Getter private Item itemAllowed;

    /**
     * Make a new instance.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param item The item to accept.
     */
    public SlotSingleItem(Container inventory, int index, int x, int y, Item item) {
        super(inventory, index, x, y);
        this.itemAllowed = item;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return super.mayPlace(itemStack) && itemStack.getItem() == getItemAllowed();
    }

}
