package org.cyclops.cyclopscore.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.itemhandler.SlotlessItemHandlerWrapper;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * A {@link org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler}
 * that uses the index from a {@link IndexedInventory}.
 * @author rubensworks
 */
public class IndexedSlotlessItemHandlerWrapper extends SlotlessItemHandlerWrapper {

    private final IInventoryIndexReference inventory;

    public IndexedSlotlessItemHandlerWrapper(IItemHandler itemHandler, IInventoryIndexReference inventory) {
        super(itemHandler);
        this.inventory = inventory;
    }

    @Override
    protected PrimitiveIterator.OfInt getNonFullSlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        if (!IngredientComponent.ITEMSTACK.getMatcher().hasCondition(matchFlags, ItemMatch.ITEM)) {
            return IntStream.range(0, itemHandler.getSlots())
                    .filter(slot -> {
                        ItemStack slotStack = itemHandler.getStackInSlot(slot);
                        return ItemMatch.areItemStacksEqual(slotStack, itemStack, matchFlags)
                                && slotStack.getCount() < itemHandler.getSlotLimit(slot)
                                && slotStack.getCount() < slotStack.getMaxStackSize();
                    })
                    .iterator();
        }

        Map<Item, Int2ObjectMap<ItemStack>> items = inventory.getIndex();
        Int2ObjectMap<ItemStack> stacks = items.get(itemStack.getItem());
        if (stacks != null) {
            return stacks.int2ObjectEntrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getCount()
                            < Math.min(inventory.getInventoryStackLimit(), entry.getValue().getMaxStackSize())
                            && ItemMatch.areItemStacksEqual(entry.getValue(), itemStack, matchFlags))
                    .mapToInt(Int2ObjectMap.Entry::getIntKey)
                    .iterator();
        }
        return IntStream.empty().iterator();
    }

    @Override
    protected PrimitiveIterator.OfInt getNonEmptySlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        if (!IngredientComponent.ITEMSTACK.getMatcher().hasCondition(matchFlags, ItemMatch.ITEM)) {
            return intIteratorToStream(getNonEmptySlots())
                    .filter(slot -> {
                        ItemStack slotStack = itemHandler.getStackInSlot(slot);
                        return !slotStack.isEmpty() && ItemMatch.areItemStacksEqual(slotStack, itemStack, matchFlags);
                    })
                    .iterator();
        }

        Map<Item, Int2ObjectMap<ItemStack>> items = inventory.getIndex();
        Int2ObjectMap<ItemStack> stacks = items.get(itemStack.getItem());
        if (stacks != null) {
            return stacks.int2ObjectEntrySet()
                    .stream()
                    .filter(entry -> ItemMatch.areItemStacksEqual(entry.getValue(), itemStack, matchFlags))
                    .mapToInt(Int2ObjectMap.Entry::getIntKey)
                    .iterator();
        }
        return IntStream.empty().iterator();
    }

    @Override
    protected PrimitiveIterator.OfInt getSlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags) {
        if (!IngredientComponent.ITEMSTACK.getMatcher().hasCondition(matchFlags, ItemMatch.ITEM)) {
            return IntStream.range(0, itemHandler.getSlots())
                    .filter(slot -> ItemMatch.areItemStacksEqual(itemHandler.getStackInSlot(slot), itemStack, matchFlags))
                    .iterator();
        }
        Map<Item, Int2ObjectMap<ItemStack>> items = inventory.getIndex();
        Int2ObjectMap<ItemStack> stacks = items.get(itemStack.getItem());
        if (stacks == null) {
            return IntStream.empty().iterator();
        }

        return stacks.int2ObjectEntrySet()
                .stream()
                .filter(entry -> ItemMatch.areItemStacksEqual(entry.getValue(), itemStack, matchFlags))
                .mapToInt(Int2ObjectMap.Entry::getIntKey)
                .iterator();
    }

    @Override
    protected PrimitiveIterator.OfInt getEmptySlots() {
        return inventory.getEmptySlots();
    }

    @Override
    protected PrimitiveIterator.OfInt getNonEmptySlots() {
        return inventory.getNonEmptySlots();
    }

    public static IntStream intIteratorToStream(PrimitiveIterator.OfInt it) {
        return StreamSupport.intStream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }

    public static interface IInventoryIndexReference {

        public int getInventoryStackLimit();
        public Map<Item, Int2ObjectMap<ItemStack>> getIndex();
        public PrimitiveIterator.OfInt getEmptySlots();
        public PrimitiveIterator.OfInt getNonEmptySlots();

    }

    public static class IndexIterator implements IntIterator {

        private final Iterator<Int2ObjectMap.Entry<ItemStack>> slotIterator;
        private final ItemStack prototype;
        private final int matchFlags;
        private int next;

        public IndexIterator(Iterator<Int2ObjectMap.Entry<ItemStack>> slotIterator, ItemStack prototype, int matchFlags) {
            this.slotIterator = slotIterator;
            this.prototype = prototype;
            this.matchFlags = matchFlags;
            this.next = findNext();
        }

        protected int findNext() {
            while(slotIterator.hasNext()) {
                Int2ObjectMap.Entry<ItemStack> entry = slotIterator.next();
                int slot = entry.getIntKey();
                ItemStack itemStack = entry.getValue();
                if (ItemMatch.areItemStacksEqual(itemStack, prototype, matchFlags)) {
                    return slot;
                }
            }
            return -1;
        }

        @Override
        public boolean hasNext() {
            return next > 0;
        }

        @Override
        public Integer next() {
            return nextInt();
        }

        @Override
        public int nextInt() {
            if (!hasNext()) {
                throw new NoSuchElementException("Slot out of bounds");
            }
            int next = this.next;
            this.next = findNext();
            return next;
        }

        @Override
        public int skip(int n) {
            int skipped = 0;
            while (n > 0 && hasNext()) {
                nextInt();
                skipped++;
            }
            return skipped;
        }
    }

    public static class WrappedIntIterator implements PrimitiveIterator.OfInt {

        private final IntIterator it;

        public WrappedIntIterator(IntIterator it) {
            this.it = it;
        }

        @Override
        public int nextInt() {
            return it.nextInt();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Integer next() {
            return it.next();
        }
    }

}
