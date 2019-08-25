package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.cyclops.cyclopscore.helper.InventoryHelpers;

import javax.annotation.Nullable;

/**
 * A container for an item.
 * @author rubensworks
 *
 * @param <I> The item instance.
 */
public abstract class ItemInventoryContainer<I extends Item> extends InventoryContainer {
	
	protected I item;
	protected int itemIndex;
	protected Hand hand;

	/**
	 * Make a new instance.
	 * @param type The container type.
	 * @param id The container id.
	 * @param inventory The player inventory.
	 * @param item The item.
	 * @param itemIndex The index of the item in use inside the player inventory.
	 */
	public ItemInventoryContainer(@Nullable ContainerType<?> type, int id,  PlayerInventory inventory, I item, int itemIndex) {
		this(type, id, inventory, item, itemIndex, Hand.MAIN_HAND);
	}

	/**
	 * Make a new instance.
	 * @param type The container type.
	 * @param id The container id.
	 * @param inventory The player inventory.
	 * @param item The item.
	 * @param itemIndex The index of the item in use inside the player inventory.
	 * @param hand The hand the player is using.
	 */
	public ItemInventoryContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, I item, int itemIndex, Hand hand) {
		super(type, id, inventory);
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
	public boolean canInteractWith(PlayerEntity player) {
		ItemStack item = getItemStack(player);
		return item != null && item.getItem() == getItem();
	}

	public ItemStack getItemStack(PlayerEntity player) {
		return InventoryHelpers.getItemFromIndex(player, itemIndex, hand);
	}
	
	@Override
	protected Slot createNewSlot(IInventory inventory, int index, int x, int y) {
    	return new Slot(inventory, index, x, y) {
    		
    		@Override
    		public boolean canTakeStack(PlayerEntity player) {
    			return this.getStack() != InventoryHelpers.getItemFromIndex(player, itemIndex, hand);
    	    }
    		
    	};
    }

	@Override
	public ItemStack slotClick(int slotId, int arg, ClickType clickType, PlayerEntity player) {
		if (clickType == ClickType.SWAP && arg == itemIndex) {
			// Don't allow swapping with the slot of the active item.
			return ItemStack.EMPTY;
		}
		return super.slotClick(slotId, arg, clickType, player);
	}
}
