package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator over all slots in an item handler.
 * @author rubensworks
 */
public class ItemHandlerItemStackIterator implements Iterator<ItemStack> {

    private final IItemHandler itemHandler;
    private final int maxSlots;
    private int slot;

    public ItemHandlerItemStackIterator(IItemHandler itemHandler, int offset) {
        this.itemHandler = itemHandler;
        // Cache the total slot count, since it can be an expensive operation on composite inventories
        this.maxSlots = itemHandler.getSlots();
        this.slot = offset;
    }

    public ItemHandlerItemStackIterator(IItemHandler itemHandler) {
        this(itemHandler, 0);
    }

    @Override
    public boolean hasNext() {
        return slot < maxSlots;
    }

    @Override
    public ItemStack next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Slot out of bounds");
        }
        return itemHandler.getStackInSlot(slot++);
    }
}
