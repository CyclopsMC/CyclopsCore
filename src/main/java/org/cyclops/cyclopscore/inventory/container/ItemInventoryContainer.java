package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import org.cyclops.cyclopscore.helper.InventoryHelpers;

import javax.annotation.Nullable;

/**
 * A container for an item.
 *
 * Implementations of this class will typically have two constructors,
 * which will look something like this:
 * <pre>
 *     // Called by the client-side screen factory
 *     public MyContainer(int id, PlayerInventory inventory, FriendlyByteBuf packetBuffer) {
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
public abstract class ItemInventoryContainer<I extends Item> extends ContainerExtended {

	protected I item;
	protected int itemIndex;
	protected InteractionHand hand;

	/**
	 * Make a new instance.
	 * @param type The container type.
	 * @param id The container id.
	 * @param inventory The player inventory.
	 * @param itemIndex The index of the item in use inside the player inventory.
	 * @param hand The hand the player is using.
	 */
	public ItemInventoryContainer(@Nullable MenuType<?> type, int id, Inventory inventory, int itemIndex, InteractionHand hand) {
		super(type, id, inventory);
		this.item = (I) InventoryHelpers.getItemFromIndex(inventory.player, itemIndex, hand).getItem();
		this.itemIndex = itemIndex;
		this.hand = hand;
	}

	public static int readItemIndex(FriendlyByteBuf packetBuffer) {
		return packetBuffer.readInt();
	}

	public static InteractionHand readHand(FriendlyByteBuf packetBuffer) {
		return packetBuffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	/**
	 * Get the item instance.
	 * @return The item.
	 */
	public I getItem() {
		return item;
	}

	@Override
	public boolean stillValid(Player player) {
		ItemStack item = getItemStack(player);
		return item != null && item.getItem() == getItem();
	}

	public ItemStack getItemStack(Player player) {
		return InventoryHelpers.getItemFromIndex(player, itemIndex, hand);
	}

	@Override
	protected Slot createNewSlot(Container inventory, int index, int x, int y) {
    	return new Slot(inventory, index, x, y) {

    		@Override
    		public boolean mayPickup(Player player) {
    			return this.getItem() != InventoryHelpers.getItemFromIndex(player, itemIndex, hand);
    	    }

    	};
    }

	@Override
	public void clicked(int slotId, int arg, ClickType clickType, Player player) {
		if (clickType == ClickType.SWAP && arg == itemIndex) {
			// Don't allow swapping with the slot of the active item.
			return;
		}
		super.clicked(slotId, arg, clickType, player);
	}
}
