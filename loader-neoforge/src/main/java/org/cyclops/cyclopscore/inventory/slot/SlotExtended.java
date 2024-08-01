package org.cyclops.cyclopscore.inventory.slot;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

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

    public SlotExtended(Container inventoryIn, int index, int x, int y) {
        super(inventoryIn, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return isActive() && container.canPlaceItem(getSlotIndex(), stack);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return super.mayPickup(playerIn) && !isPhantom();
    }
}
