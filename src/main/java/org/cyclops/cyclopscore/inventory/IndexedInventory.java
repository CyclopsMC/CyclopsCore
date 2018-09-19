package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

/**
 * An inventory that adds an index from item to slot on a regular inventory.
 * @author rubensworks
 *
 */
public class IndexedInventory extends LargeInventory implements IndexedSlotlessItemHandlerWrapper.IInventoryIndexReference {

    private final Map<Item, Int2ObjectMap<ItemStack>> index = Maps.newIdentityHashMap();
    private int firstEmptySlot;
    private int lastEmptySlot;
    private int firstNonEmptySlot;
    private int lastNonEmptySlot;

    /**
     * Default constructor for NBT persistence, don't call this yourself.
     */
    public IndexedInventory() {
        this(0, "", 0);
    }

    /**
     * Make a new instance.
     * @param size The amount of slots in the inventory.
     * @param name The name of the inventory, used for NBT storage.
     * @param stackLimit The stack limit for each slot.
     */
    public IndexedInventory(int size, String name, int stackLimit) {
        super(size, name, stackLimit);
        this.firstEmptySlot = 0;
        this.lastEmptySlot = size - 1;
        this.firstNonEmptySlot = -1;
        this.lastNonEmptySlot = -1;
    }

    protected void createIndex() {
        index.clear();
        firstEmptySlot = -1;
        lastEmptySlot = -1;
        firstNonEmptySlot = -1;
        lastNonEmptySlot = -1;
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack itemStack = getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                Int2ObjectMap<ItemStack> stacks = index.get(itemStack.getItem());
                if (stacks == null) {
                    stacks = new Int2ObjectOpenHashMap<>();
                    index.put(itemStack.getItem(), stacks);
                }
                stacks.put(i, itemStack);
                if (firstNonEmptySlot < 0) {
                    firstNonEmptySlot = i;
                }
                lastNonEmptySlot = i;
            } else {
                if (firstEmptySlot < 0) {
                    firstEmptySlot = i;
                }
                lastEmptySlot = i;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound data, String tag) {
        super.readFromNBT(data, tag);
        createIndex();
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemStack) {
        // Update index
        ItemStack oldStack = getStackInSlot(slotId);
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
        if (slotId == firstEmptySlot && oldStack.isEmpty() && !itemStack.isEmpty()) {
            // Search forwards
            int oldFirstEmptySlot = firstEmptySlot;
            firstEmptySlot = -1;
            for (int i = Math.max(0, oldFirstEmptySlot); i < getSizeInventory(); i++) {
                if (getStackInSlot(i).isEmpty()) {
                    firstEmptySlot = i;
                    break;
                }
            }
        }
        if (slotId == lastEmptySlot && oldStack.isEmpty() && !itemStack.isEmpty()) {
            // Search backwards
            int oldLastEmptySlot = lastEmptySlot;
            lastEmptySlot = -1;
            for (int i = oldLastEmptySlot; i >= 0; i--) {
                if (getStackInSlot(i).isEmpty()) {
                    lastEmptySlot = i;
                    break;
                }
            }
        }
        if (slotId == firstNonEmptySlot && !oldStack.isEmpty() && itemStack.isEmpty()) {
            // Search forwards
            int oldFirstNonEmptySlot = firstNonEmptySlot;
            firstNonEmptySlot = -1;
            for (int i = Math.max(0, oldFirstNonEmptySlot); i < getSizeInventory(); i++) {
                if (!getStackInSlot(i).isEmpty()) {
                    firstNonEmptySlot = i;
                    break;
                }
            }
        }
        if (slotId == lastNonEmptySlot && !oldStack.isEmpty() && itemStack.isEmpty()) {
            // Search backwards
            int oldLastNonEmptySlot = lastNonEmptySlot;
            lastNonEmptySlot = -1;
            for (int i = oldLastNonEmptySlot; i >= 0; i--) {
                if (!getStackInSlot(i).isEmpty()) {
                    lastNonEmptySlot = i;
                    break;
                }
            }
        }
        if ((slotId < firstEmptySlot || firstEmptySlot < 0) && itemStack.isEmpty()) {
            firstEmptySlot = slotId;
        }
        if (slotId > lastEmptySlot && itemStack.isEmpty()) {
            lastEmptySlot = slotId;
        }
        if ((slotId < firstNonEmptySlot || firstNonEmptySlot < 0) && !itemStack.isEmpty())  {
            firstNonEmptySlot = slotId;
        }
        if (slotId > lastNonEmptySlot && !itemStack.isEmpty())  {
            lastNonEmptySlot = slotId;
        }

        // This is unit-tested, so this *should not* be able to happen.
        // If it happens, trigger a crash!
        if (firstEmptySlot == firstNonEmptySlot) throw new IllegalStateException(String.format(
                "Indexed inventory at inconsistent with first empty %s and first non-empty %s.", firstEmptySlot, firstNonEmptySlot));
        if (lastEmptySlot == lastNonEmptySlot) throw new IllegalStateException(String.format(
                "Indexed inventory at inconsistent with last empty %s and last non-empty %s.", lastEmptySlot, lastNonEmptySlot));
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
    public int getFirstEmptySlot() {
        return firstEmptySlot;
    }

    @Override
    public int getLastEmptySlot() {
        return lastEmptySlot;
    }

    @Override
    public int getFirstNonEmptySlot() {
        return firstNonEmptySlot;
    }

    @Override
    public int getLastNonEmptySlot() {
        return lastNonEmptySlot;
    }
}