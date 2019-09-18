package org.cyclops.cyclopscore.inventory;

import org.cyclops.commoncapabilities.api.capability.inventorystate.IInventoryState;

/**
 * An inventory state implementation for a {@link SimpleInventory}.
 * @author rubensworks
 */
public class SimpleInventoryState implements IInventoryState {

    private final SimpleInventory inventory;

    public SimpleInventoryState(SimpleInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public int getState() {
        return this.inventory.getState();
    }

}
