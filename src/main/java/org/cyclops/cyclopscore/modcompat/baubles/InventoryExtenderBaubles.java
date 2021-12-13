package org.cyclops.cyclopscore.modcompat.baubles;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;

import javax.annotation.Nullable;

/**
 * Extends the iteratable inventory with Baubles slots.
 */
public class InventoryExtenderBaubles implements PlayerExtendedInventoryIterator.IInventoryExtender {

    @Override
    @Nullable
    public IItemHandlerModifiable getInventory(PlayerEntity player) {
        return null; // TODO
        //return BaublesAPI.getBaublesHandler(player).orElse(null);
    }
}
