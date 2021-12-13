package org.cyclops.cyclopscore.inventory.slot;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * A slot with some fancy and fun extra features.
 * @author rubensworks
 */
public class SlotExtended extends Slot {

    @Getter
    @Setter
    private boolean enabled = true;

    @Getter
    @Setter
    private boolean phantom = false;

    @Getter
    @Setter
    private boolean adjustable = true;

    public SlotExtended(IInventory inventoryIn, int index, int x, int y) {
        super(inventoryIn, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return isActive() && container.canPlaceItem(getSlotIndex(), stack);
    }

    @Override
    public boolean mayPickup(PlayerEntity playerIn) {
        return super.mayPickup(playerIn) && !isPhantom();
    }
}
