package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.inventory.InventoryLocationPlayer;
import org.cyclops.cyclopscore.inventory.ItemLocation;

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
    protected ItemLocation itemLocation;

    /**
     * Make a new instance.
     * @param type The container type.
     * @param id The container id.
     * @param inventory The player inventory.
     * @param itemLocation The item location.
     */
    public ItemInventoryContainer(@Nullable MenuType<?> type, int id, Inventory inventory, ItemLocation itemLocation) {
        super(type, id, inventory);
        this.item = (I) itemLocation.getItemStack(inventory.player).getItem();
        this.itemLocation = itemLocation;
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
        return this.itemLocation.getItemStack(player);
    }

    @Override
    protected Slot createNewSlot(Container inventory, int index, int x, int y) {
        return new Slot(inventory, index, x, y) {

            @Override
            public boolean mayPickup(Player player) {
                return this.getItem() != itemLocation.getItemStack(player);
            }

        };
    }

    @Override
    public void clicked(int slotId, int arg, ClickType clickType, Player player) {
        if (clickType == ClickType.SWAP && itemLocation.inventoryLocation() == InventoryLocationPlayer.getInstance() && arg == itemLocation.slot()) {
            // Don't allow swapping with the slot of the active item.
            return;
        }
        super.clicked(slotId, arg, clickType, player);
    }
}
