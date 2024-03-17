package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

/**
 * Iterate over a player's inventory.
 * @author rubensworks
 *
 */
public class PlayerInventoryIterator extends InventoryIterator {


    public PlayerInventoryIterator(Player player) {
        super(new InvWrapper(player.getInventory()));
    }
}
