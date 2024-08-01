package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * A simplified form of the {@link net.neoforged.neoforge.items.IItemHandler} that is slot-agnostic.
 * By not taking into account slots, the item handler provider instead of the consumer
 * is responsible for providing an efficient item insertion and extraction algorithm.
 *
 * Note: The capability provider MUST ensure deterministic behaviour for extraction and insertion.
 *       For example, the three first returned stacks hereafter must equal each other.
 *       <pre>
 *          ItemStack stack1 = insertItem(myStack, true);
 *          ItemStack stack2 = insertItem(myStack, true);
 *          ItemStack stack3 = insertItem(myStack, false);
 *          ItemStack stack4 = insertItem(myStack, true); // Can be different again
 *       </pre>
 *       The same applies to extraction.
 *
 * @author rubensworks
 */
public interface ISlotlessItemHandler {

    /**
     * @return An immutable iterator over all available ingredients in this storage.
     */
    public Iterator<ItemStack> getItems();

    /**
     * Find all ItemStacks matching the given stack from the item handler.
     *
     * Calling this method will not modify the storage in any way.
     * Results from this method MUST NOT be modified.
     *
     * @param stack      The ItemStack to search for.
     * @param matchFlags The flags to compare the given matchStack by according to {@link ItemMatch}.
     *                   ItemMatch.DAMAGE | ItemMatch.NBT will for instance make sure to only extract
     *                   items that have exactly the same damage value and nbt tag, while ignoring the stacksize.
     * @return An immutable iterator over ItemStacks that match the given stack, which may potentially be empty.
     */
    Iterator<ItemStack> findItems(@Nonnull ItemStack stack, int matchFlags);

    /**
     * Inserts an ItemStack into the item handler and return the remainder.
     * The ItemStack should not be modified in this function!
     * Note: This behaviour is subtly different from IFluidHandlers.fill()
     *
     * @param stack    ItemStack to insert.
     * @param simulate If true, the insertion is only simulated
     * @return The remaining ItemStack that was not inserted (if the entire stack is accepted, then return {@link ItemStack#EMPTY}).
     *         May be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
     **/
    @Nonnull
    ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate);

    /**
     * Extracts an ItemStack from the item handler. The returned value must be null
     * if nothing is extracted, otherwise it's stack size must not be greater than amount or the
     * itemstacks getMaxStackSize().
     *
     * @param amount   Amount to extract (may be greater than the current stacks max limit)
     * @param simulate If true, the extraction is only simulated
     * @return ItemStack extracted from the slot, must be {@link ItemStack#EMPTY}, if nothing can be extracted
     **/
    @Nonnull
    ItemStack extractItem(int amount, boolean simulate);

    /**
     * Extract an ItemStack matching the given stack from the item handler.
     * If nothing is extracted, otherwise it's stack size must not be greater than the itemstacks getMaxStackSize()
     *
     * If the stacksize is ignored according to the matchFlags,
     * then the stacksize of the given matchStack MUST be interpreted
     * as the maximum quantity that must be extracted.
     *
     * @param matchStack The ItemStack to search for.
     * @param matchFlags The flags to compare the given matchStack by according to {@link ItemMatch}.
     *                   ItemMatch.DAMAGE | ItemMatch.NBT will for instance make sure to only extract
     *                   items that have exactly the same damage value and nbt tag, while ignoring the stacksize.
     * @param simulate   If true, the insertion is only simulated
     * @return ItemStack extracted from the slot, must be {@link ItemStack#EMPTY}, if nothing can be extracted
     */
    @Nonnull
    ItemStack extractItem(@Nonnull ItemStack matchStack, int matchFlags, boolean simulate);

    /**
     * Retrieves the total item stack count allowed to exist in this handler.
     *
     * @return The maximum item count allowed in the handler.
     */
    int getLimit();

}
