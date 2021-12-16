package org.cyclops.cyclopscore.modcompat.baubles;

import lazy.baubles.api.BaublesAPI;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;

import javax.annotation.Nullable;

/**
 * Extends the iteratable inventory with Baubles slots.
 */
public class InventoryExtenderBaubles implements PlayerExtendedInventoryIterator.IInventoryExtender {

    @Override
    @Nullable
    public IItemHandlerModifiable getInventory(Player player) {
        return BaublesAPI.getBaublesHandler(player).orElse(null);
    }
}
