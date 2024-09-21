package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.entity.player.Player;

/**
 * Iterate over a player's inventory.
 * @author rubensworks
 *
 */
public class PlayerInventoryIterator extends InventoryIterator {

    public PlayerInventoryIterator(Player player) {
        super(new InventoryCommonModifiableContainer(player.getInventory()));
    }
}
