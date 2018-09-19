package org.cyclops.cyclopscore.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.itemhandler.SlotlessItemHandlerWrapper;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

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

    @Override
    protected int getNonFullSlotWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        if (!IngredientComponent.ITEMSTACK.getMatcher().hasCondition(matchFlags, ItemMatch.ITEM)) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack slotStack = itemHandler.getStackInSlot(i);
                if (ItemMatch.areItemStacksEqual(slotStack, itemStack, matchFlags)
                        && slotStack.getCount() < itemHandler.getSlotLimit(i)
                        && slotStack.getCount() < slotStack.getMaxStackSize()) {
                    return i;
                }
            }
            return -1;
        }

        Map<Item, Int2ObjectMap<ItemStack>> items = inventory.getIndex();
        Int2ObjectMap<ItemStack> stacks = items.get(itemStack.getItem());
        if (stacks != null) {
            for (Int2ObjectMap.Entry<ItemStack> entry : stacks.int2ObjectEntrySet()) {
                ItemStack value = entry.getValue();
                if (value.getCount() < Math.min(inventory.getInventoryStackLimit(), value.getMaxStackSize())
                        && ItemMatch.areItemStacksEqual(value, itemStack, matchFlags)) {
                    return entry.getIntKey();
                }
            }
        }
        return -1;
    }

    @Override
    protected int getNonEmptySlotWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        if (!IngredientComponent.ITEMSTACK.getMatcher().hasCondition(matchFlags, ItemMatch.ITEM)) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack slotStack = itemHandler.getStackInSlot(i);
                if (!slotStack.isEmpty() && ItemMatch.areItemStacksEqual(slotStack, itemStack, matchFlags)) {
                    return i;
                }
            }
            return -1;
        }

        Map<Item, Int2ObjectMap<ItemStack>> items = inventory.getIndex();
        Int2ObjectMap<ItemStack> stacks = items.get(itemStack.getItem());
        if (stacks != null) {
            for (Int2ObjectMap.Entry<ItemStack> entry : stacks.int2ObjectEntrySet()) {
                ItemStack value = entry.getValue();
                if (ItemMatch.areItemStacksEqual(value, itemStack, matchFlags)) {
                    return entry.getIntKey();
                }
            }
        }
        return -1;
    }

    @Override
    protected Iterator<Integer> getSlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        if (!IngredientComponent.ITEMSTACK.getMatcher().hasCondition(matchFlags, ItemMatch.ITEM)) {
            return IntStream.range(0, itemHandler.getSlots())
                    .filter(slot -> ItemMatch.areItemStacksEqual(itemHandler.getStackInSlot(slot), itemStack, matchFlags))
                    .iterator();
        }
        Map<Item, Int2ObjectMap<ItemStack>> items = inventory.getIndex();
        Int2ObjectMap<ItemStack> stacks = items.get(itemStack.getItem());
        if (stacks == null) {
            return Collections.emptyIterator();
        }
        return new IndexIterator(stacks.int2ObjectEntrySet().iterator(), itemStack, matchFlags);
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
        public Map<Item, Int2ObjectMap<ItemStack>> getIndex();
        public int getFirstEmptySlot();
        public int getLastEmptySlot();
        public int getFirstNonEmptySlot();
        public int getLastNonEmptySlot();

    }

    public static class IndexIterator implements Iterator<Integer> {

        private final Iterator<Int2ObjectMap.Entry<ItemStack>> slotIterator;
        private final ItemStack prototype;
        private final int matchFlags;
        private Integer next;

        public IndexIterator(Iterator<Int2ObjectMap.Entry<ItemStack>> slotIterator, ItemStack prototype, int matchFlags) {
            this.slotIterator = slotIterator;
            this.prototype = prototype;
            this.matchFlags = matchFlags;
            this.next = findNext();
        }

        protected Integer findNext() {
            while(slotIterator.hasNext()) {
                Int2ObjectMap.Entry<ItemStack> entry = slotIterator.next();
                int slot = entry.getIntKey();
                ItemStack itemStack = entry.getValue();
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
