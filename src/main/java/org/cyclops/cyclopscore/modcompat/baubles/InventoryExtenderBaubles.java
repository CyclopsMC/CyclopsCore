package org.cyclops.cyclopscore.modcompat.baubles;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;

/**
 * Extends the iteratable inventory with Baubles slots.
 */
public class InventoryExtenderBaubles implements PlayerExtendedInventoryIterator.IInventoryExtender {

    @Override
    public IItemHandlerModifiable getInventory(PlayerEntity player) {
        return BaublesApi.getBaublesHandler(player);
    }
}
