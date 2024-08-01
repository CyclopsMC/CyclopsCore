package org.cyclops.commoncapabilities.api.capability.itemhandler;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.PrimitiveIterator;

/**
 * An abstract {@link ISlotlessItemHandler} wrapper around an {@link IItemHandler}.
 * @author rubensworks
 */
public abstract class SlotlessItemHandlerWrapper implements ISlotlessItemHandler {

    protected final IItemHandler itemHandler;

    public SlotlessItemHandlerWrapper(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    /**
     * Get the slots in which the given ItemStack is present according to the given match flags.
     * Stacksize of the item in the slot must be below the maximum stack size,
     * so there must be room left in the slot.
     * @param itemStack The ItemStack to look for.
     * @param matchFlags The flags to match the given ItemStack by.
     * @return The slots in which the ItemStack are present.
     */
    protected abstract PrimitiveIterator.OfInt getNonFullSlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags);

    /**
     * Get the slots in which the given ItemStack is present according to the given match flags.
     * Stacksize of the item in the slot must be larger than zero.
     * @param itemStack The ItemStack to look for.
     * @param matchFlags The flags to match the given ItemStack by.
     * @return The slots in which the ItemStack are present.
     */
    protected abstract PrimitiveIterator.OfInt getNonEmptySlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags);

    /**
     * Get an iterator over all slots in which the given ItemStack is present according to the given match flags.
     * Stacksize of the item in the slot must be larger than zero.
     * @param itemStack The ItemStack to look for.
     * @param matchFlags The flags to match the given ItemStack by.
     * @return An iterator over all slots in which the ItemStack is present.
     */
    protected abstract PrimitiveIterator.OfInt getSlotsWithItemStack(@Nonnull ItemStack itemStack, int matchFlags);

    /**
     * @return The slots with no ItemStack.
     */
    protected abstract PrimitiveIterator.OfInt getEmptySlots();

    /**
     * @return The slots that are not empty.
     */
    protected abstract PrimitiveIterator.OfInt getNonEmptySlots();

    @Override
    public Iterator<ItemStack> getItems() {
        return new ItemHandlerItemStackIterator(itemHandler);
    }

    @Override
    public Iterator<ItemStack> findItems(@Nonnull ItemStack stack, int matchFlags) {
        return Iterators.transform(getSlotsWithItemStack(stack, matchFlags), new Function<Integer, ItemStack>() {
            @Nullable
            @Override
            public ItemStack apply(@Nullable Integer input) {
                return itemHandler.getStackInSlot(input);
            }
        });
    }

    @Override
    @Nonnull
    public ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate) {
        ItemStack stackSimulate = stack;

        // First, do a simulated insertion without mutating anything.
        PrimitiveIterator.OfInt itNonFull = getNonFullSlotsWithItemStack(stackSimulate, ItemMatch.ITEM | ItemMatch.DATA);
        IntList applicableSlots = simulate ? null : new IntArrayList();
        while (itNonFull.hasNext() && !stackSimulate.isEmpty()) {
            int slot = itNonFull.nextInt();
            int countPre = stackSimulate.getCount();
            stackSimulate = itemHandler.insertItem(slot, stackSimulate, true);
            int countPost = stackSimulate.getCount();
            if (!simulate && countPre != countPost) {
                applicableSlots.add(slot);
            }
        }

        if (!stackSimulate.isEmpty()) {
            PrimitiveIterator.OfInt itEmpty = getEmptySlots();
            while (itEmpty.hasNext() && !stackSimulate.isEmpty()) {
                int slot = itEmpty.nextInt();
                int countPre = stackSimulate.getCount();
                stackSimulate = itemHandler.insertItem(slot, stackSimulate, true);
                int countPost = stackSimulate.getCount();
                if (!simulate && countPre != countPost) {
                    applicableSlots.add(slot);
                }
            }
        }

        // Next, if not simulating, mutate the actual slots
        // We do this separate from the iterator to avoid concurrent modification.
        if (!simulate) {
            IntListIterator it = applicableSlots.iterator();
            while (it.hasNext() && !stack.isEmpty()) {
                int slot = it.nextInt();
                stack = itemHandler.insertItem(slot, stack, false);
            }
            return stack;
        } else {
            return stackSimulate;
        }
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int amount, boolean simulate) {
        int amountSimulate = amount;

        // First, do a simulated extraction without mutating anything.
        PrimitiveIterator.OfInt itSimulated = getNonEmptySlots();
        IntList applicableSlots = simulate ? null : new IntArrayList();
        ItemStack extractedAcc = ItemStack.EMPTY;
        while (itSimulated.hasNext() && amountSimulate > 0) {
            int slot = itSimulated.nextInt();
            ItemStack extracted = itemHandler.extractItem(slot, amountSimulate, true);
            if (!extracted.isEmpty()) {
                if (extractedAcc.isEmpty()) {
                    extractedAcc = extracted.copy();
                    amountSimulate -= extracted.getCount();
                    if (!simulate) {
                        applicableSlots.add(slot);
                    }
                } else if (ItemMatch.areItemStacksEqual(extracted, extractedAcc, ItemMatch.ITEM | ItemMatch.DATA)) {
                    amountSimulate -= extracted.getCount();
                    extractedAcc.grow(extracted.getCount());
                    if (!simulate) {
                        applicableSlots.add(slot);
                    }
                }
            }
        }

        // Next, if not simulating, mutate the actual slots
        // We do this separate from the iterator to avoid concurrent modification.
        if (!simulate) {
            IntListIterator it = applicableSlots.iterator();
            extractedAcc = ItemStack.EMPTY;
            while (it.hasNext() && amount > 0) {
                int slot = it.nextInt();
                ItemStack extractedSimulated = itemHandler.extractItem(slot, amount, true); // This *should* not be needed, but I guess you can never trust other mods...
                if (!extractedSimulated.isEmpty()) {
                    if (extractedAcc.isEmpty()) {
                        ItemStack extracted = itemHandler.extractItem(slot, amount, false);
                        extractedAcc = extracted.copy();
                        amount -= extracted.getCount();
                    } else if (ItemMatch.areItemStacksEqual(extractedSimulated, extractedAcc, ItemMatch.ITEM | ItemMatch.DATA)) {
                        ItemStack extracted = itemHandler.extractItem(slot, amount, false);
                        amount -= extracted.getCount();
                        extractedAcc.grow(extracted.getCount());
                    }
                }
            }
        }

        return extractedAcc;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(@Nonnull ItemStack matchStack, int matchFlags, boolean simulate) {
        // First, do a simulated extraction without mutating anything.
        PrimitiveIterator.OfInt itSimulated = getNonEmptySlotsWithItemStack(matchStack, matchFlags);
        IntList applicableSlots = simulate ? null : new IntArrayList();
        int amount = matchStack.getCount();
        ItemStack extractedAcc = ItemStack.EMPTY;
        while (itSimulated.hasNext() && amount > 0) {
            int slot = itSimulated.nextInt();
            ItemStack extracted = itemHandler.extractItem(slot, amount, true);
            if (!extracted.isEmpty()) {
                if (extractedAcc.isEmpty()) {
                    extractedAcc = extracted.copy();
                    amount -= extracted.getCount();
                    if (!simulate) {
                        applicableSlots.add(slot);
                    }
                } else if (ItemMatch.areItemStacksEqual(extracted, extractedAcc, matchFlags & ~ItemMatch.STACKSIZE)) {
                    amount -= extracted.getCount();
                    extractedAcc.grow(extracted.getCount());
                    if (!simulate) {
                        applicableSlots.add(slot);
                    }
                }
            }
        }


        // Next, if not simulating, mutate the actual slots
        // We do this separate from the iterator to avoid concurrent modification.
        if (!simulate) {
            IntListIterator it = applicableSlots.iterator();
            amount = matchStack.getCount();
            extractedAcc = ItemStack.EMPTY;
            while (it.hasNext() && amount > 0) {
                int slot = it.nextInt();
                ItemStack extractedSimulated = itemHandler.extractItem(slot, amount, true); // This *should* not be needed, but I guess you can never trust other mods...
                if (!extractedSimulated.isEmpty()) {
                    if (extractedAcc.isEmpty()) {
                        ItemStack extracted = itemHandler.extractItem(slot, amount, false);
                        extractedAcc = extracted.copy();
                        amount -= extracted.getCount();
                    } else if (ItemMatch.areItemStacksEqual(extractedSimulated, extractedAcc, matchFlags & ~ItemMatch.STACKSIZE)) {
                        ItemStack extracted = itemHandler.extractItem(slot, amount, false);
                        amount -= extracted.getCount();
                        extractedAcc.grow(extracted.getCount());
                    }
                }
            }
        }

        return extractedAcc;
    }

    @Override
    public int getLimit() {
        int total = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            total += itemHandler.getSlotLimit(i);
        }
        return total;
    }
}
