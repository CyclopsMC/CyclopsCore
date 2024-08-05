package org.cyclops.cyclopscore.component;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.inventory.SimpleInventory;

/**
 * @author rubensworks
 */
public class DataComponentInventoryConfig extends DataComponentConfigCommon<SimpleInventory, ModBase<?>> {
    public DataComponentInventoryConfig() {
        super(CyclopsCore._instance, "inventory", builder -> builder
                .persistent(SimpleInventory.CODEC)
                .networkSynchronized(SimpleInventory.STREAM_CODEC));
    }
}
