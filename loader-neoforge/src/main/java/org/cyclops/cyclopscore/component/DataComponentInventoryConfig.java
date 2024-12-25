package org.cyclops.cyclopscore.component;

import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.inventory.SimpleInventoryCommon;

/**
 * @author rubensworks
 */
public class DataComponentInventoryConfig extends DataComponentConfigCommon<SimpleInventoryCommon, ModBaseNeoForge<?>> {
    public DataComponentInventoryConfig() {
        super(CyclopsCoreNeoForge._instance, "inventory", builder -> builder
                .persistent(SimpleInventoryCommon.CODEC)
                .networkSynchronized(SimpleInventoryCommon.STREAM_CODEC));
    }
}
