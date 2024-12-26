package org.cyclops.cyclopscore.component;

import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.inventory.SimpleInventory;

/**
 * @author rubensworks
 */
public class DataComponentInventoryConfig extends DataComponentConfigCommon<SimpleInventory, ModBaseNeoForge<?>> {
    public DataComponentInventoryConfig() {
        super(CyclopsCoreNeoForge._instance, "inventory", builder -> builder
                .persistent(SimpleInventory.CODEC)
                .networkSynchronized(SimpleInventory.STREAM_CODEC));
    }
}
