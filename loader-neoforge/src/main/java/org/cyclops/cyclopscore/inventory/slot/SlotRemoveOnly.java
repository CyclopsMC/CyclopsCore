package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * A slot from which the player can only remove an item, not place one inside it.
 * @author rubensworks
 *
 */
public class SlotRemoveOnly extends SlotExtended {

    /**
     * Make a new instance.
     * @param inventory The inventory for which the slot applies.
     * @param index The index the slot is at.
     * @param x The X coordinate for the slot to render at.
     * @param y The Y coordinate for the slot to render at.
     */
    public SlotRemoveOnly(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

}
