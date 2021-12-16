package org.cyclops.cyclopscore.capability.item;

import net.minecraft.world.Container;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.cyclops.cyclopscore.inventory.InventorySlotMasked;

/**
 * An item handler that only exposes a given number of slots.
 * @author rubensworks
 */
public class ItemHandlerSlotMasked extends InvWrapper {

    public ItemHandlerSlotMasked(Container inventory, int... slots) {
        super(new InventorySlotMasked(inventory, slots));
    }

}
