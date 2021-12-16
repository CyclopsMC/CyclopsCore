package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Iterate over a player's inventory and any other attached inventory like baubles.
 * @author rubensworks
 */
public class PlayerExtendedInventoryIterator implements Iterator<ItemStack> {

    public static final List<IInventoryExtender> INVENTORY_EXTENDERS = Lists.newArrayList();

    private final Queue<InventoryIterator> iterators;

    /**
     * Create a new HotbarIterator.
     * @param player The player to iterate the hotbar from.
     */
    public PlayerExtendedInventoryIterator(Player player) {
        this.iterators = Queues.newArrayDeque();
        iterators.add(new PlayerInventoryIterator(player));
        for (IInventoryExtender inventoryExtender : PlayerExtendedInventoryIterator.INVENTORY_EXTENDERS) {
            IItemHandlerModifiable inv = inventoryExtender.getInventory(player);
            if (inv != null) {
                iterators.add(new InventoryIterator(inv));
            }
        }
    }

    @Override
    public boolean hasNext() {
        return iterators.size() > 0 && iterators.peek().hasNext();
    }

    @Override
    public ItemStack next() {
        if (iterators.peek().hasNext()) {
            ItemStack next = iterators.peek().next();
            if (!iterators.peek().hasNext()) {
                iterators.poll();
            }
            return next;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void remove() {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Replaces the itemstack on the position of the last returned itemstack.
     * @param itemStack The itemstack to place.
     */
    public void replace(ItemStack itemStack) {
        iterators.peek().replace(itemStack);
    }

    /**
     * A registerable inventory extender for iterating over.
     */
    public static interface IInventoryExtender {

        @Nullable
        public IItemHandlerModifiable getInventory(Player player);

    }

}
