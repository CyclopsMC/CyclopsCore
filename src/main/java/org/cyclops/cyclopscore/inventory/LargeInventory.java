package org.cyclops.cyclopscore.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

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
    public void readFromNBT(CompoundNBT data, String tag) {
        ListNBT nbttaglist = data.getList(tag, Constants.NBT.TAG_COMPOUND);

        for (int j = 0; j < getSizeInventory(); ++j)
            contents[j] = ItemStack.EMPTY;

        for (int j = 0; j < nbttaglist.size(); ++j) {
            CompoundNBT slot = nbttaglist.getCompound(j);
            int index;
            if (slot.contains("index")) {
                index = slot.getInt("index");
            } else {
                index = slot.getInt("Slot");
            }
            if (index >= 0 && index < getSizeInventory()) {
                contents[index] = ItemStack.read(slot);
            }
        }
    }

    /**
     * Write inventory data to the given NBT.
     * @param data The NBT tag that will receive inventory data.
     * @param tag The NBT tag name where the info must be located.
     */
    public void writeToNBT(CompoundNBT data, String tag) {
        ListNBT slots = new ListNBT();
        for (int index = 0; index < getSizeInventory(); ++index) {
            ItemStack itemStack = getStackInSlot(index);
            if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                CompoundNBT slot = new CompoundNBT();
                slots.add(slot);
                slot.putInt("Slot", index);
                itemStack.write(slot);
            }
        }
        data.put(tag, slots);
    }

}