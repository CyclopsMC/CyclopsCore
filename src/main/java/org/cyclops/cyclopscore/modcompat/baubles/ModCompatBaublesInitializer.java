package org.cyclops.cyclopscore.modcompat.baubles;

import org.cyclops.cyclopscore.inventory.InventoryLocations;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;

public class ModCompatBaublesInitializer implements ICompatInitializer {

    @Override
    public void initialize() {
        InventoryLocations.REGISTRY.register(new InventoryLocationBaubles());
    }

}
