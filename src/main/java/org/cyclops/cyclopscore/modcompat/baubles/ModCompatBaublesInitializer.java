package org.cyclops.cyclopscore.modcompat.baubles;

import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;

public class ModCompatBaublesInitializer implements ICompatInitializer {

    @Override
    public void initialize() {
        PlayerExtendedInventoryIterator.INVENTORY_EXTENDERS.add(new InventoryExtenderBaubles());
    }

}
