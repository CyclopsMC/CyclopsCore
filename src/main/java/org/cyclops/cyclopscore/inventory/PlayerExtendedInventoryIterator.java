package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Queues;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Iterator;
import java.util.Queue;

/**
 * Iterate over a player's inventory and any other attached inventory like baubles.
 * @author rubensworks
 */
public class PlayerExtendedInventoryIterator implements Iterator<ItemStack> {

    private final Queue<Pair<IInventoryLocation, InventoryIterator>> iterators;

    /**
     * Create a new HotbarIterator.
     * @param player The player to iterate the hotbar from.
     */
    public PlayerExtendedInventoryIterator(Player player) {
        this.iterators = Queues.newArrayDeque();
        for (IInventoryLocation inventoryExtender : InventoryLocations.REGISTRY.values()) {
            IItemHandlerModifiable inv = inventoryExtender.getInventory(player);
            if (inv != null) {
                iterators.add(Pair.of(inventoryExtender, new InventoryIterator(inv)));
            }
        }
    }

    @Override
    public boolean hasNext() {
        return iterators.size() > 0 && iterators.peek().getRight().hasNext();
    }

    @Override
    public ItemStack next() {
        if (iterators.peek().getRight().hasNext()) {
            Pair<IInventoryLocation, InventoryIterator> extendedAndIterator = iterators.peek();
            Pair<Integer, ItemStack> slotAndStack = extendedAndIterator.getRight().nextIndexed();
            if (!iterators.peek().getRight().hasNext()) {
                iterators.poll();
            }
            return slotAndStack.getRight();
        }
        throw new IndexOutOfBoundsException();
    }

    public ItemLocation nextIndexed() {
        if (iterators.peek().getRight().hasNext()) {
            Pair<IInventoryLocation, InventoryIterator> extendedAndIterator = iterators.peek();
            Pair<Integer, ItemStack> slotAndStack = extendedAndIterator.getRight().nextIndexed();
            if (!iterators.peek().getRight().hasNext()) {
                iterators.poll();
            }
            return new ItemLocation(extendedAndIterator.getLeft(), slotAndStack.getLeft());
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
        iterators.peek().getRight().replace(itemStack);
    }

}
