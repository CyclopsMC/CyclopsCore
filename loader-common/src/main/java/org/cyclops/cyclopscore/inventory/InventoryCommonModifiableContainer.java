package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * @author rubensworks
 */
public class InventoryCommonModifiableContainer implements IInventoryCommonModifiable {

    private final Container container;

    public InventoryCommonModifiableContainer(Container container) {
        this.container = container;
    }

    @Override
    public int getSlots() {
        return this.container.getContainerSize();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.container.getItem(slot);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack itemStack) {
        this.container.setItem(slot, itemStack);
    }
}
