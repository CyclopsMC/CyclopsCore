package org.cyclops.cyclopscore.component;

import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * @author rubensworks
 */
public class DataComponentFluidContentConfig extends DataComponentConfigCommon<SimpleFluidContent, ModBaseNeoForge<?>> {
    public DataComponentFluidContentConfig() {
        super(CyclopsCoreNeoForge._instance, "fluid_content", builder -> builder
                .persistent(SimpleFluidContent.CODEC)
                .networkSynchronized(SimpleFluidContent.STREAM_CODEC));
    }
}
