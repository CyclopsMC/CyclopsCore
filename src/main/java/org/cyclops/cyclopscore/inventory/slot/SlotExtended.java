package org.cyclops.cyclopscore.inventory.slot;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * A slot with some fancy and fun extra features.
 * @author rubensworks
 */
public class SlotExtended extends Slot {

    @Getter
    @Setter
    private boolean enabled = true;

    public SlotExtended(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return isEnabled() && inventory.isItemValidForSlot(getSlotIndex(), stack);
    }

}
