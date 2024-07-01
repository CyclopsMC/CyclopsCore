package org.cyclops.cyclopscore.inventory;

import net.minecraft.core.HolderLookup;
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

    public void readFromNBT(HolderLookup.Provider provider, CompoundTag data, String tag) {
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
                contents[index] = ItemStack.parseOptional(provider, slot);
            }
        }
    }

    public void writeToNBT(HolderLookup.Provider provider, CompoundTag data, String tag) {
        ListTag slots = new ListTag();
        for (int index = 0; index < getContainerSize(); ++index) {
            ItemStack itemStack = getItem(index);
            if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                CompoundTag slot = new CompoundTag();
                slots.add(slot);
                slot.putInt("Slot", index);
                itemStack.save(provider, slot);
            }
        }
        data.put(tag, slots);
    }

}
