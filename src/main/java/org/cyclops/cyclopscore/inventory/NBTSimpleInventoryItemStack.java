package org.cyclops.cyclopscore.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.RegistryEntries;
import org.cyclops.cyclopscore.helper.InventoryHelpers;

/**
 * A simple inventory for an ItemStack that can be stored in NBT.
 * @author rubensworks
 *
 */
public class NBTSimpleInventoryItemStack extends SimpleInventory {

    protected final ItemStack itemStack;
    protected final String tagName;

    /**
     * Make a new instance.
     * @param itemStack The item stack.
     * @param size The amount of slots in the inventory.
     * @param stackLimit The stack limit for each slot.
     * @param tagName The NBT tag name to store this inventory in.
     *                This should be the same tag name that is used to call the NBT read/write methods.
     */
    public NBTSimpleInventoryItemStack(ItemStack itemStack, int size, int stackLimit, String tagName) {
        super(size, stackLimit);
        this.itemStack = itemStack;
        this.tagName = tagName;

        SimpleInventory contents = itemStack.get(RegistryEntries.COMPONENT_INVENTORY);
        if (contents != null) {
            for (int i = 0; i < contents.getContainerSize(); i++) {
                setItem(i, contents.getItem(i));
            }
        }
    }

    @Override
    public void setChanged() {
        itemStack.set(RegistryEntries.COMPONENT_INVENTORY, this);
    }

    @Override
    public void readFromNBT(HolderLookup.Provider provider, CompoundTag data, String tagName) {
        InventoryHelpers.readFromNBT(provider, this, data, tagName);
    }

    @Override
    public void writeToNBT(HolderLookup.Provider provider, CompoundTag data, String tagName) {
        InventoryHelpers.writeToNBT(provider, this, data, tagName);
    }

}
