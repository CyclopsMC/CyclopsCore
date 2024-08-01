package org.cyclops.cyclopscore.inventory;

import org.cyclops.cyclopscore.CyclopsCore;

/**
 * @author rubensworks
 */
public class InventoryLocations {

    public static IRegistryInventoryLocation REGISTRY = CyclopsCore._instance.getRegistryManager().getRegistry(IRegistryInventoryLocation.class);

    static {
        REGISTRY.register(InventoryLocationPlayer.getInstance());
    }

}
