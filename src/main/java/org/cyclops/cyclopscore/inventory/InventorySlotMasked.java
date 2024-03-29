package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * An inventory that only exposes a given number of slots.
 * @author rubensworks
 */
public class InventorySlotMasked implements Container {

    private final Container inventory;
    private final int[] slots;

    public InventorySlotMasked(Container inventory, int... slots) {
        this.inventory = inventory;
        this.slots = slots;
    }

    protected void validateSlot(int slot) {
        if (slot < 0 || slot >= getContainerSize()) {
            throw new IndexOutOfBoundsException(String.format("Tried to get slot %s from %s slots.", slot, getContainerSize()));
        }
    }

    protected int externalToInternalSlot(int externalSlot) {
        validateSlot(externalSlot);
        return this.slots[externalSlot];
    }

    @Override
    public int getContainerSize() {
        return this.slots.length;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return inventory.getItem(externalToInternalSlot(index));
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return inventory.removeItem(externalToInternalSlot(index), count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return inventory.removeItemNoUpdate(externalToInternalSlot(index));
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        inventory.setItem(externalToInternalSlot(index), stack);
    }

    @Override
    public int getMaxStackSize() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void setChanged() {
        inventory.setChanged();;
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }

    @Override
    public void startOpen(Player player) {
        inventory.startOpen(player);
    }

    @Override
    public void stopOpen(Player player) {
        inventory.stopOpen(player);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return inventory.canPlaceItem(externalToInternalSlot(index), stack);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            removeItemNoUpdate(i);
        }
    }
}
