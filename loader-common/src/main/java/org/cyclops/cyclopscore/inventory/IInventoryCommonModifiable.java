package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.item.ItemStack;

/**
 * @author rubensworks
 */
public interface IInventoryCommonModifiable {
    int getSlots();

    ItemStack getStackInSlot(int slot);

    void setStackInSlot(int slot, ItemStack itemStack);
}
