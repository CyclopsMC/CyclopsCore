package org.cyclops.cyclopscore.modcompat.baubles;

import org.cyclops.cyclopscore.inventory.RegistryInventoryLocation;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;

public class ModCompatBaublesInitializer implements ICompatInitializer {

    @Override
    public void initialize() {
        RegistryInventoryLocation.getInstance().register(new InventoryLocationBaubles());
    }

}
