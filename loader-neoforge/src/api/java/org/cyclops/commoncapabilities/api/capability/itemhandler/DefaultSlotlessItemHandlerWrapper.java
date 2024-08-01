package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * An naive {@link ISlotlessItemHandler} wrapper around an {@link IItemHandler}.
 * This will perform a LIFO item algorithm.
 * @author rubensworks
 */
public class DefaultSlotlessItemHandlerWrapper implements ISlotlessItemHandler {

    private final IItemHandler itemHandler;

    public DefaultSlotlessItemHandlerWrapper(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public Iterator<ItemStack> getItems() {
        return new ItemHandlerItemStackIterator(getItemHandler());
    }

    @Override
    public Iterator<ItemStack> findItems(@Nonnull ItemStack stack, int matchFlags) {
        return new FilteredItemHandlerItemStackIterator(getItemHandler(), stack, matchFlags);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate) {
        for (int i = 0; i < getItemHandler().getSlots(); i++) {
            stack = getItemHandler().insertItem(i, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int amount, boolean simulate) {
        IItemHandler itemHandler = getItemHandler();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack itemStack = itemHandler.extractItem(i, amount, simulate);
            if (!itemStack.isEmpty()) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(@Nonnull ItemStack matchStack, int matchFlags, boolean simulate) {
        IItemHandler itemHandler = getItemHandler();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            int amount = matchStack.getCount();
            ItemStack tempItemStack;
            if (simulate || (!(tempItemStack = itemHandler.extractItem(i, amount, true)).isEmpty()
                    && ItemMatch.areItemStacksEqual(matchStack, tempItemStack, matchFlags))) {
                ItemStack itemStack = itemHandler.extractItem(i, amount, simulate);
                if (!itemStack.isEmpty() && ItemMatch.areItemStacksEqual(matchStack, itemStack, matchFlags)) {
                    return itemStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getLimit() {
        IItemHandler itemHandler = getItemHandler();
        int total = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            total += itemHandler.getSlotLimit(i);
        }
        return total;
    }
}
