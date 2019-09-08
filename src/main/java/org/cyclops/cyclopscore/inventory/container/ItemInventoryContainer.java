package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import org.cyclops.cyclopscore.helper.InventoryHelpers;

import javax.annotation.Nullable;

/**
 * A container for an item.
 *
 * Implementations of this class will typically have two constructors,
 * which will look something like this:
 * <pre>
 *     // Called by the client-side screen factory
 *     public MyContainer(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
 *         this(id, inventory, readItemIndex(packetBuffer), readHand(packetBuffer));
 *     }
 *
 *     // Called by the server-side container provider
 *     public MyContainer(int id, PlayerInventory inventory, int itemIndex, Hand hand) {
 *         super(RegistryEntries.CONTAINER_MY, id, inventory, itemIndex, hand);
 *     }
 * </pre>
 *
 * @param <I> The item instance.
 * @author rubensworks
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
	 * @param itemIndex The index of the item in use inside the player inventory.
	 * @param hand The hand the player is using.
	 */
	public ItemInventoryContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, int itemIndex, Hand hand) {
		super(type, id, inventory);
		this.item = (I) InventoryHelpers.getItemFromIndex(inventory.player, itemIndex, hand).getItem();
		this.itemIndex = itemIndex;
		this.hand = hand;
	}

	public static int readItemIndex(PacketBuffer packetBuffer) {
		return packetBuffer.readInt();
	}

	public static Hand readHand(PacketBuffer packetBuffer) {
		return packetBuffer.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
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
