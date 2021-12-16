package org.cyclops.cyclopscore.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

/**
 * A large inventory implementation.
 * @author rubensworks
 *
 */
public class LargeInventory extends SimpleInventory {

    /**
     * Default constructor for NBT persistence, don't call this yourself.
     */
    public LargeInventory() {
        this(0, 0);
    }

    /**
     * Make a new instance.
     * @param size The amount of slots in the inventory.
     * @param stackLimit The stack limit for each slot.
     */
    public LargeInventory(int size, int stackLimit) {
        super(size, stackLimit);
    }

    /**
     * Read inventory data from the given NBT.
     * @param data The NBT data containing inventory data.
     * @param tag The NBT tag name where the info is located.
     */
    public void readFromNBT(CompoundTag data, String tag) {
        ListTag nbttaglist = data.getList(tag, Tag.TAG_COMPOUND);

        for (int j = 0; j < getContainerSize(); ++j)
            contents[j] = ItemStack.EMPTY;

        for (int j = 0; j < nbttaglist.size(); ++j) {
            CompoundTag slot = nbttaglist.getCompound(j);
            int index;
            if (slot.contains("index")) {
                index = slot.getInt("index");
            } else {
                index = slot.getInt("Slot");
            }
            if (index >= 0 && index < getContainerSize()) {
                contents[index] = ItemStack.of(slot);
            }
        }
    }

    /**
     * Write inventory data to the given NBT.
     * @param data The NBT tag that will receive inventory data.
     * @param tag The NBT tag name where the info must be located.
     */
    public void writeToNBT(CompoundTag data, String tag) {
        ListTag slots = new ListTag();
        for (int index = 0; index < getContainerSize(); ++index) {
            ItemStack itemStack = getItem(index);
            if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                CompoundTag slot = new CompoundTag();
                slots.add(slot);
                slot.putInt("Slot", index);
                itemStack.save(slot);
            }
        }
        data.put(tag, slots);
    }

}