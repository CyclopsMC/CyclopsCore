package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A filtered iterator over all slots in an item handler.
 * @author rubensworks
 */
public class FilteredItemHandlerItemStackIterator implements Iterator<ItemStack> {

    private final IItemHandler itemHandler;
    private final ItemStack prototype;
    private final int matchFlags;
    private int slot = 0;
    private ItemStack next;

    public FilteredItemHandlerItemStackIterator(IItemHandler itemHandler, ItemStack prototype, int matchFlags) {
        this.itemHandler = itemHandler;
        this.prototype = prototype;
        this.matchFlags = matchFlags;
        this.next = findNext();
    }

    protected ItemStack findNext() {
        while(slot < itemHandler.getSlots()) {
            ItemStack itemStack = itemHandler.getStackInSlot(slot++);
            if (ItemMatch.areItemStacksEqual(itemStack, prototype, matchFlags)) {
                return itemStack;
            }
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public ItemStack next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Slot out of bounds");
        }
        ItemStack next = this.next;
        this.next = findNext();
        return next;
    }
}
