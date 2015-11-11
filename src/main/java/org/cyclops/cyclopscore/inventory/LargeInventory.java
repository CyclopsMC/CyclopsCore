package org.cyclops.cyclopscore.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * A large inventory implementation.
 * @author rubensworks
 *
 */
public class LargeInventory extends SimpleInventory {

    /**
     * Make a new instance.
     * @param size The amount of slots in the inventory.
     * @param name The name of the inventory, used for NBT storage.
     * @param stackLimit The stack limit for each slot.
     */
    public LargeInventory(int size, String name, int stackLimit) {
        super(size, name, stackLimit);
    }

    /**
     * Read inventory data from the given NBT.
     * @param data The NBT data containing inventory data.
     * @param tag The NBT tag name where the info is located.
     */
    public void readFromNBT(NBTTagCompound data, String tag) {
        NBTTagList nbttaglist = data.getTagList(tag, MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());

        for (int j = 0; j < _contents.length; ++j)
            _contents[j] = null;

        for (int j = 0; j < nbttaglist.tagCount(); ++j) {
            NBTTagCompound slot = nbttaglist.getCompoundTagAt(j);
            int index;
            if (slot.hasKey("index")) {
                index = slot.getInteger("index");
            } else {
                index = slot.getInteger("Slot");
            }
            if (index >= 0 && index < _contents.length) {
                _contents[index] = ItemStack.loadItemStackFromNBT(slot);
            }
        }
    }

    /**
     * Write inventory data to the given NBT.
     * @param data The NBT tag that will receive inventory data.
     * @param tag The NBT tag name where the info must be located.
     */
    public void writeToNBT(NBTTagCompound data, String tag) {
        NBTTagList slots = new NBTTagList();
        for (int index = 0; index < _contents.length; ++index) {
            if (_contents[index] != null && _contents[index].stackSize > 0) {
                NBTTagCompound slot = new NBTTagCompound();
                slots.appendTag(slot);
                slot.setInteger("Slot", index);
                _contents[index].writeToNBT(slot);
            }
        }
        data.setTag(tag, slots);
    }

}