package org.cyclops.cyclopscore.inventory;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.itemhandler.SlotlessItemHandlerWrapper;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A {@link org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler}
 * that uses the index from a {@link IndexedInventory}.
 * @author rubensworks
 */
public class IndexedSlotlessItemHandlerWrapper extends SlotlessItemHandlerWrapper {

    private final IInventoryIndexReference inventory;
    private MovementStrategy inputStrategy;
    private MovementStrategy outputStrategy;

    public IndexedSlotlessItemHandlerWrapper(IItemHandler itemHandler, IInventoryIndexReference inventory,
                                             MovementStrategy inputStrategy, MovementStrategy outputStrategy) {
        super(itemHandler);
        this.inventory = inventory;
        this.inputStrategy = inputStrategy;
        this.outputStrategy = outputStrategy;
    }

    public IndexedSlotlessItemHandlerWrapper(IItemHandler itemHandler, IInventoryIndexReference inventory) {
        this(itemHandler, inventory, MovementStrategy.FIRST, MovementStrategy.FIRST);
    }

    // TODO: check if match flags contain ItemMatch.ITEM! If not, iterate over slots in the naive way.

    @Override
    protected int getNonFullSlotWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        Map<Item, TIntObjectMap<ItemStack>> items = inventory.getIndex();
        TIntObjectMap<ItemStack> stacks = items.get(itemStack.getItem());
        if (stacks != null) {
            for (TIntObjectIterator<ItemStack> it = stacks.iterator(); it.hasNext(); ) {
                it.advance();
                if (it.value().getCount() < Math.min(inventory.getInventoryStackLimit(), it.value().getMaxStackSize())
                        && ItemMatch.areItemStacksEqual(it.value(), itemStack, matchFlags)) {
                    return it.key();
                }
            }
        }
        return -1;
    }

    @Override
    protected int getNonEmptySlotWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        Map<Item, TIntObjectMap<ItemStack>> items = inventory.getIndex();
        TIntObjectMap<ItemStack> stacks = items.get(itemStack.getItem());
        if (stacks != null) {
            for (TIntObjectIterator<ItemStack> it = stacks.iterator(); it.hasNext(); ) {
                it.advance();
                if (ItemMatch.areItemStacksEqual(it.value(), itemStack, matchFlags)) {
                    return it.key();
                }
            }
        }
        return -1;
    }

    @Override
    protected Iterator<Integer> getSlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        Map<Item, TIntObjectMap<ItemStack>> items = inventory.getIndex();
        TIntObjectMap<ItemStack> stacks = items.get(itemStack.getItem());
        if (stacks == null) {
            return Collections.emptyIterator();
        }
        return new IndexIterator(stacks.iterator(), itemStack, matchFlags);
    }

    @Override
    protected int getEmptySlot() {
        return inputStrategy == MovementStrategy.FIRST ? inventory.getFirstEmptySlot() : inventory.getLastEmptySlot();
    }

    @Override
    protected int getNonEmptySlot() {
        return outputStrategy == MovementStrategy.FIRST ? inventory.getFirstNonEmptySlot() : inventory.getLastNonEmptySlot();
    }

    public MovementStrategy getInputStrategy() {
        return inputStrategy;
    }

    public void setInputStrategy(MovementStrategy inputStrategy) {
        this.inputStrategy = inputStrategy;
    }

    public MovementStrategy getOutputStrategy() {
        return outputStrategy;
    }

    public void setOutputStrategy(MovementStrategy outputStrategy) {
        this.outputStrategy = outputStrategy;
    }

    /**
     * Strategies indicating which slots to pick first.
     */
    public static enum MovementStrategy {
        /**
         * Pick the earliest possible slot, with the smallest slot id.
         */
        FIRST,
        /**
         * Pick the latest possible slot, with the largest slot id.
         */
        LAST
    }

    public static interface IInventoryIndexReference {

        public int getInventoryStackLimit();
        public Map<Item, TIntObjectMap<ItemStack>> getIndex();
        public int getFirstEmptySlot();
        public int getLastEmptySlot();
        public int getFirstNonEmptySlot();
        public int getLastNonEmptySlot();

    }

    public static class IndexIterator implements Iterator<Integer> {

        private final TIntObjectIterator<ItemStack> slotIterator;
        private final ItemStack prototype;
        private final int matchFlags;
        private Integer next;

        public IndexIterator(TIntObjectIterator<ItemStack> slotIterator, ItemStack prototype, int matchFlags) {
            this.slotIterator = slotIterator;
            this.prototype = prototype;
            this.matchFlags = matchFlags;
            this.next = findNext();
        }

        protected Integer findNext() {
            while(slotIterator.hasNext()) {
                int slot = slotIterator.key();
                ItemStack itemStack = slotIterator.value();
                slotIterator.advance();
                if (ItemMatch.areItemStacksEqual(itemStack, prototype, matchFlags)) {
                    return slot;
                }
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Slot out of bounds");
            }
            Integer next = this.next;
            this.next = findNext();
            return next;
        }
    }

}
