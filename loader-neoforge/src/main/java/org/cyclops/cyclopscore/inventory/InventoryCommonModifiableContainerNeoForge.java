package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

/**
 * @author rubensworks
 */
public class InventoryCommonModifiableContainerNeoForge implements IInventoryCommonModifiable {

    private final IItemHandlerModifiable itemHandler;

    public InventoryCommonModifiableContainerNeoForge(IItemHandlerModifiable itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public int getSlots() {
        return this.itemHandler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.itemHandler.getStackInSlot(slot);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack itemStack) {
        this.itemHandler.setStackInSlot(slot, itemStack);
    }
}
