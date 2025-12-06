package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * An item access backed by an item location.
 * @author rubensworks
 */
public class ItemAccessItemLocation implements ItemAccess {

    private final Player player;
    private final ItemLocation itemLocation;

    public ItemAccessItemLocation(Player player, ItemLocation itemLocation) {
        this.player = player;
        this.itemLocation = itemLocation;
    }

    @Override
    public ItemResource getResource() {
        return ItemResource.of(itemLocation.getItemStack(player));
    }

    @Override
    public int getAmount() {
        return itemLocation.getItemStack(player).getCount();
    }

    @Override
    public int insert(ItemResource itemResource, int amount, TransactionContext transactionContext) {
        itemLocation.setItemStack(player, itemResource.toStack(amount));
        return amount;
    }

    @Override
    public int extract(ItemResource itemResource, int amount, TransactionContext transactionContext) {
        itemLocation.setItemStack(player, ItemStack.EMPTY);
        return amount;
    }
}
