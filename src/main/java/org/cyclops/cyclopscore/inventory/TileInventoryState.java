package org.cyclops.cyclopscore.inventory;

import org.cyclops.commoncapabilities.api.capability.inventorystate.IInventoryState;
import org.cyclops.cyclopscore.tileentity.InventoryTileEntityBase;

/**
 * {@link IInventoryState} implementation that refers to a {@link InventoryTileEntityBase}.
 * @author rubensworks
 */
public class TileInventoryState implements IInventoryState {

    private final InventoryTileEntityBase tile;

    public TileInventoryState(InventoryTileEntityBase tile) {
        this.tile = tile;
    }

    @Override
    public int getHash() {
        return tile.getInventoryHash();
    }
}
