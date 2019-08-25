package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.Map;
import java.util.PrimitiveIterator;

/**
 * An inventory that adds an index from item to slot on a regular inventory.
 * @author rubensworks
 *
 */
public class IndexedInventory extends LargeInventory implements IndexedSlotlessItemHandlerWrapper.IInventoryIndexReference {

    private final Map<Item, Int2ObjectMap<ItemStack>> index = Maps.newIdentityHashMap();
    private IntSet emptySlots;
    private IntSet nonEmptySlots;

    /**
     * Default constructor for NBT persistence, don't call this yourself.
     */
    public IndexedInventory() {
        this(0, 0);
    }

    /**
     * Make a new instance.
     * @param size The amount of slots in the inventory.
     * @param stackLimit The stack limit for each slot.
     */
    public IndexedInventory(int size, int stackLimit) {
        super(size, stackLimit);
        this.emptySlots = new IntAVLTreeSet();
        this.nonEmptySlots = new IntAVLTreeSet();
        createIndex();
    }

    protected void createIndex() {
        this.index.clear();
        this.nonEmptySlots.clear();
        this.emptySlots.clear();
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack itemStack = getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                Int2ObjectMap<ItemStack> stacks = index.get(itemStack.getItem());
                if (stacks == null) {
                    stacks = new Int2ObjectOpenHashMap<>();
                    index.put(itemStack.getItem(), stacks);
                }
                stacks.put(i, itemStack);
                this.nonEmptySlots.add(i);
            } else {
                this.emptySlots.add(i);
            }
        }
    }

    @Override
    public void readFromNBT(CompoundNBT data, String tag) {
        super.readFromNBT(data, tag);
        createIndex();
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemStack) {
        // Update index
        ItemStack oldStack = getStackInSlot(slotId);
        boolean wasEmpty = oldStack.isEmpty();
        boolean isEmpty = itemStack.isEmpty();
        if (!oldStack.isEmpty()) {
            Int2ObjectMap<ItemStack> stacks = index.get(oldStack.getItem());
            if (stacks != null) {
                stacks.remove(slotId);
            }
            if (stacks.isEmpty()) {
                index.remove(oldStack.getItem());
            }
        }
        if (!itemStack.isEmpty()) {
            Int2ObjectMap<ItemStack> stacks = index.get(itemStack.getItem());
            if (stacks == null) {
                stacks = new Int2ObjectOpenHashMap<>();
                index.put(itemStack.getItem(), stacks);
            }
            stacks.put(slotId, itemStack);
        }

        // Call super
        super.setInventorySlotContents(slotId, itemStack);

        // Update first and last values
        if (wasEmpty && !isEmpty) {
            this.emptySlots.remove(slotId);
            this.nonEmptySlots.add(slotId);
        }
        if (!wasEmpty && isEmpty) {
            this.emptySlots.add(slotId);
            this.nonEmptySlots.remove(slotId);
        }

        // This is unit-tested, so this *should not* be able to happen.
        // If it happens, trigger a crash!
        if (this.nonEmptySlots.size() + this.emptySlots.size() != getSizeInventory()) throw new IllegalStateException(String.format(
                "Indexed inventory at inconsistent state %s %s %s (slot: %s).", this.nonEmptySlots, this.emptySlots, this.getSizeInventory(), slotId));
    }

    @Override
    public void clear() {
        super.clear();
        index.clear();
    }

    @Override
    public Map<Item, Int2ObjectMap<ItemStack>> getIndex() {
        return index;
    }

    @Override
    public PrimitiveIterator.OfInt getEmptySlots() {
        return new IndexedSlotlessItemHandlerWrapper.WrappedIntIterator(this.emptySlots.iterator());
    }

    @Override
    public PrimitiveIterator.OfInt getNonEmptySlots() {
        return new IndexedSlotlessItemHandlerWrapper.WrappedIntIterator(this.nonEmptySlots.iterator());
    }
}