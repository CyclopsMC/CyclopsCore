package org.cyclops.cyclopscore.inventory;

import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Iterator;

/**
 * Iterate over a player's inventory.
 * @author rubensworks
 *
 */
public class PlayerInventoryIterator implements Iterator<ItemStack>{
    
    @Getter private final EntityPlayer player;
    private int i;
    
    /**
     * Create a new HotbarIterator.
     * @param player The player to iterate the hotbar from.
     */
    public PlayerInventoryIterator(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public boolean hasNext() {
        return i < player.inventory.mainInventory.size();
    }

    @Override
    public ItemStack next() {
        return player.inventory.mainInventory.get(i++);
    }
    
    /**
     * Get the next item indexed.
     * @return The indexed item.
     */
    public Pair<Integer, ItemStack> nextIndexed() {
    	return Pair.of(i, next());
    }

    @Override
    public void remove() {
        if(i - 1 >= 0 && i - 1 < player.inventory.mainInventory.size())
            player.inventory.mainInventory.set(i - 1, ItemStack.EMPTY);
    }

    /**
     * Replaces the itemstack on the position of the last returned itemstack.
     * @param itemStack The itemstack to place.
     */
    public void replace(ItemStack itemStack) {
        if(i - 1 >= 0 && i - 1 < player.inventory.mainInventory.size())
            player.inventory.mainInventory.set(i - 1, itemStack);
    }

}
