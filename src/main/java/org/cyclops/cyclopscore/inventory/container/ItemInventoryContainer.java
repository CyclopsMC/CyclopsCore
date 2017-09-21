package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.inventory.IGuiContainerProviderConfigurable;

/**
 * A container for an item.
 * @author rubensworks
 *
 * @param <I> The item instance.
 */
public abstract class ItemInventoryContainer<I extends Item & IGuiContainerProviderConfigurable> extends InventoryContainerConfigurable {
	
	protected I item;
	protected int itemIndex;
	protected EnumHand hand;

	/**
	 * Make a new instance.
	 * @param inventory The player inventory.
	 * @param item The item.
	 * @param itemIndex The index of the item in use inside the player inventory.
	 */
	public ItemInventoryContainer(InventoryPlayer inventory, I item, int itemIndex) {
		this(inventory, item, itemIndex, EnumHand.MAIN_HAND);
	}

	/**
	 * Make a new instance.
	 * @param inventory The player inventory.
	 * @param item The item.
	 * @param itemIndex The index of the item in use inside the player inventory.
	 * @param hand The hand the player is using.
	 */
	public ItemInventoryContainer(InventoryPlayer inventory, I item, int itemIndex, EnumHand hand) {
		super(inventory, item);
		this.item = item;
		this.itemIndex = itemIndex;
		this.hand = hand;
	}

	/**
	 * Get the item instance.
	 * @return The item.
	 */
	public I getItem() {
		return item;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		ItemStack item = getItemStack(player);
		return item != null && item.getItem() == getItem();
	}

	public ItemStack getItemStack(EntityPlayer player) {
		return InventoryHelpers.getItemFromIndex(player, itemIndex, hand);
	}
	
	@Override
	protected Slot createNewSlot(IInventory inventory, int index, int x, int y) {
    	return new Slot(inventory, index, x, y) {
    		
    		@Override
    		public boolean canTakeStack(EntityPlayer player) {
    			return this.getStack() != InventoryHelpers.getItemFromIndex(player, itemIndex, hand);
    	    }
    		
    	};
    }
	
}
