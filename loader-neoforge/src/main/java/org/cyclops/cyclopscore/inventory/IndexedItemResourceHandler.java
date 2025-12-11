package org.cyclops.cyclopscore.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

/**
 * A {@link ResourceHandler} for items that uses the index from a {@link IndexedInventory}.
 * Inspired by VanillaContainerWrapper
 * @see IndexedSlotlessItemHandlerWrapper
 * @author rubensworks
 */
public class IndexedItemResourceHandler implements ResourceHandler<ItemResource> {

    private final IInventoryIndexReference indexReference;
    private final ResourceHandler<ItemResource> resourceHandler;

    public IndexedItemResourceHandler(IInventoryIndexReference indexReference, ResourceHandler<ItemResource> resourceHandler) {
        this.indexReference = indexReference;
        this.resourceHandler = resourceHandler;
    }

    @Override
    public int size() {
        return resourceHandler.size();
    }

    @Override
    public ItemResource getResource(int index) {
        return resourceHandler.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return resourceHandler.getAmountAsLong(index);
    }

    @Override
    public int getAmountAsInt(int index) {
        return resourceHandler.getAmountAsInt(index);
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return resourceHandler.getCapacityAsLong(index, resource);
    }

    @Override
    public int getCapacityAsInt(int index, ItemResource resource) {
        return resourceHandler.getCapacityAsInt(index, resource);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return resourceHandler.isValid(index, resource);
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return resourceHandler.insert(index, resource, amount, transaction);
    }

    protected PrimitiveIterator.OfInt getNonFullSlotsWithItemStack(ItemResource resource) {
        Map<Item, Int2ObjectMap<ItemStack>> items = indexReference.getIndex();
        Int2ObjectMap<ItemStack> stacks = items.get(resource.getItem());
        if (stacks != null) {
            return stacks.int2ObjectEntrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getCount()
                            < Math.min(indexReference.getInventoryReferenceStackLimit(), entry.getValue().getMaxStackSize())
                            && resource.matches(entry.getValue()))
                    .mapToInt(Int2ObjectMap.Entry::getIntKey)
                    .iterator();
        }
        return IntStream.empty().iterator();
    }

    @Override
    public int insert(ItemResource resource, int amount, TransactionContext transaction) {
        int amountOriginal = amount;

        // First, insert into slots that already contain this item
        PrimitiveIterator.OfInt itNonFull = getNonFullSlotsWithItemStack(resource);
        while (itNonFull.hasNext() && amount > 0) {
            int slot = itNonFull.nextInt();
            int inserted = this.insert(slot, resource, amount, transaction);
            amount -= inserted;
        }

        // Second, insert into empty slots
        if (amount > 0) {
            PrimitiveIterator.OfInt itEmpty = indexReference.getEmptySlots();
            while (itEmpty.hasNext() && amount > 0) {
                int slot = itEmpty.nextInt();
                int inserted = this.insert(slot, resource, amount, transaction);
                amount -= inserted;
            }
        }

        return amountOriginal - amount;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return resourceHandler.extract(index, resource, amount, transaction);
    }

    protected PrimitiveIterator.OfInt getNonEmptySlotsWithItemStack(ItemResource resource) {
        Map<Item, Int2ObjectMap<ItemStack>> items = indexReference.getIndex();
        Int2ObjectMap<ItemStack> stacks = items.get(resource.getItem());
        if (stacks != null) {
            return stacks.int2ObjectEntrySet()
                    .stream()
                    .filter(entry -> resource.matches(entry.getValue()))
                    .mapToInt(Int2ObjectMap.Entry::getIntKey)
                    .iterator();
        }
        return IntStream.empty().iterator();
    }

    @Override
    public int extract(ItemResource resource, int amount, TransactionContext transaction) {
        // First, extract from non-empty slots that contain the item.
        PrimitiveIterator.OfInt itSimulated = getNonEmptySlotsWithItemStack(resource);
        int extracted = 0;
        while (itSimulated.hasNext() && amount > 0) {
            int slot = itSimulated.nextInt();
            extracted += this.extract(slot, resource, amount - extracted, transaction);
            if (extracted >= amount) {
                break;
            }
        }
        return extracted;
    }
}
