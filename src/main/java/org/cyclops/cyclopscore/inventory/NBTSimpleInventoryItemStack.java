package org.cyclops.cyclopscore.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
		InventoryHelpers.validateNBTStorage(this, itemStack, this.tagName);
	}
	
	@Override
	public void setChanged() {
		CompoundNBT tag = itemStack.getOrCreateTag();
		writeToNBT(tag, this.tagName);
		itemStack.setTag(tag);
	}
	
	@Override
	public void readFromNBT(CompoundNBT data, String tagName) {
        InventoryHelpers.readFromNBT(this, data, tagName);
    }
	
	@Override
	public void writeToNBT(CompoundNBT data, String tagName) {
        InventoryHelpers.writeToNBT(this, data, tagName);
    }

}
