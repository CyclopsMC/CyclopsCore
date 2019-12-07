package org.cyclops.cyclopscore.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * An inventory that only exposes a given number of slots.
 * @author rubensworks
 */
public class InventorySlotMasked implements IInventory {

    private final IInventory inventory;
    private final int[] slots;

    public InventorySlotMasked(IInventory inventory, int... slots) {
        this.inventory = inventory;
        this.slots = slots;
    }

    protected void validateSlot(int slot) {
        if (slot < 0 || slot >= getSizeInventory()) {
            throw new IndexOutOfBoundsException(String.format("Tried to get slot %s from %s slots.", slot, getSizeInventory()));
        }
    }

    protected int externalToInternalSlot(int externalSlot) {
        validateSlot(externalSlot);
        return this.slots[externalSlot];
    }

    @Override
    public int getSizeInventory() {
        return this.slots.length;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getSizeInventory(); i++) {
            if (!getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.getStackInSlot(externalToInternalSlot(index));
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return inventory.decrStackSize(externalToInternalSlot(index), count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return inventory.removeStackFromSlot(externalToInternalSlot(index));
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.setInventorySlotContents(externalToInternalSlot(index), stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
        inventory.markDirty();;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return inventory.isUsableByPlayer(player);
    }

    @Override
    public void openInventory(PlayerEntity player) {
        inventory.openInventory(player);
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        inventory.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return inventory.isItemValidForSlot(externalToInternalSlot(index), stack);
    }

    @Override
    public void clear() {
        for (int i = 0; i < getSizeInventory(); i++) {
            removeStackFromSlot(i);
        }
    }
}
