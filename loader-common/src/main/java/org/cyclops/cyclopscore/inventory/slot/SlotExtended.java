package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * A slot with some fancy and fun extra features.
 *
 * @author rubensworks
 */
public class SlotExtended extends Slot {

    private boolean enabled = true;

    private boolean phantom = false;

    private boolean adjustable = true;

    public SlotExtended(Container inventoryIn, int index, int x, int y) {
        super(inventoryIn, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return isActive() && container.canPlaceItem(index, stack);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return super.mayPickup(playerIn) && !isPhantom();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isPhantom() {
        return this.phantom;
    }

    public boolean isAdjustable() {
        return this.adjustable;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPhantom(boolean phantom) {
        this.phantom = phantom;
    }

    public void setAdjustable(boolean adjustable) {
        this.adjustable = adjustable;
    }
}
