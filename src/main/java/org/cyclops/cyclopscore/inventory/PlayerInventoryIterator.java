package org.cyclops.cyclopscore.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.wrapper.InvWrapper;

/**
 * Iterate over a player's inventory.
 * @author rubensworks
 *
 */
public class PlayerInventoryIterator extends InventoryIterator {


    public PlayerInventoryIterator(PlayerEntity player) {
        super(new InvWrapper(player.inventory));
    }
}
