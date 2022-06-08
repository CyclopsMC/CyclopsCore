package org.cyclops.cyclopscore.modcompat.curios;

import org.cyclops.cyclopscore.inventory.InventoryLocations;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;

/**
 * @author rubensworks
 */
public class ModCompatCuriosInitializer implements ICompatInitializer {
    @Override
    public void initialize() {
        // Extend player iterator
        InventoryLocations.REGISTRY.register(new InventoryLocationCurios());
    }
}
