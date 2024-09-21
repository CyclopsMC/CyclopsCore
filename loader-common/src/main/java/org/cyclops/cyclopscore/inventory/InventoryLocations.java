package org.cyclops.cyclopscore.inventory;

import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;

/**
 * @author rubensworks
 */
public class InventoryLocations {

    public static IRegistryInventoryLocation REGISTRY = CyclopsCoreInstance.MOD.getRegistryManager().getRegistry(IRegistryInventoryLocation.class);

    static {
        REGISTRY.register(InventoryLocationPlayer.getInstance());
    }

}
