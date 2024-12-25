package org.cyclops.cyclopscore.inventory;

import org.cyclops.commoncapabilities.api.capability.inventorystate.IInventoryState;

/**
 * An inventory state implementation for a {@link SimpleInventoryCommon}.
 * @author rubensworks
 */
public class SimpleInventoryState implements IInventoryState {

    private final SimpleInventoryCommon inventory;

    public SimpleInventoryState(SimpleInventoryCommon inventory) {
        this.inventory = inventory;
    }

    @Override
    public int getState() {
        return this.inventory.getState();
    }

}
