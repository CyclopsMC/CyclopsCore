package org.cyclops.cyclopscore.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import org.cyclops.cyclopscore.helper.InventoryHelpers;

/**
 * A simple inventory for a currently held item by a player that can be stored in NBT.
 * @author rubensworks
 *
 */
public class NBTSimpleInventoryItemHeld extends SimpleInventory {
	
	protected final PlayerEntity player;
	protected final int itemIndex;
	protected final Hand hand;
	protected final String tagName;

	/**
	 * Make a new instance.
	 * @param player The player holding the item.
	 * @param itemIndex The index of the item in use inside the player inventory.
	 * @param size The amount of slots in the inventory.
	 * @param stackLimit The stack limit for each slot.
	 * @param tagName The NBT tag name to store this inventory in.
	 *                This should be the same tag name that is used to call the NBT read/write methods.
	 */
	public NBTSimpleInventoryItemHeld(PlayerEntity player, int itemIndex, int size, int stackLimit, String tagName) {
		this(player, itemIndex, Hand.MAIN_HAND, size, stackLimit, tagName);
	}
	
	/**
     * Make a new instance.
	 * @param player The player holding the item.
	 * @param itemIndex The index of the item in use inside the player inventory.
	 * @param hand The hand the player is using.
	 * @param size The amount of slots in the inventory.
	 * @param stackLimit The stack limit for each slot.
	 * @param tagName The NBT tag name to store this inventory in.
	 *                This should be the same tag name that is used to call the NBT read/write methods.
	 */
	public NBTSimpleInventoryItemHeld(PlayerEntity player, int itemIndex, Hand hand, int size, int stackLimit, String tagName) {
		super(size, stackLimit);
		this.player = player;
		this.itemIndex = itemIndex;
		this.hand = hand;
		this.tagName = tagName;
		InventoryHelpers.validateNBTStorage(this, InventoryHelpers.getItemFromIndex(player, itemIndex, hand), this.tagName);
	}
	
	@Override
	public void markDirty() {
		ItemStack itemStack = InventoryHelpers.getItemFromIndex(player, itemIndex, hand);
		CompoundNBT tag = itemStack.getOrCreateTag();
		writeToNBT(tag, this.tagName);
		InventoryHelpers.getItemFromIndex(player, itemIndex, hand).setTag(tag);
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
