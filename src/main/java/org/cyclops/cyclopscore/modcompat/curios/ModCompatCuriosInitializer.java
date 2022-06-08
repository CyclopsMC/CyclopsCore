package org.cyclops.cyclopscore.modcompat.curios;

import org.cyclops.cyclopscore.inventory.RegistryInventoryLocation;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;

/**
 * @author rubensworks
 */
public class ModCompatCuriosInitializer implements ICompatInitializer {
    @Override
    public void initialize() {
        // Extend player iterator
        RegistryInventoryLocation.getInstance().register(new InventoryLocationCurios());
    }
}
