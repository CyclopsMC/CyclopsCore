package org.cyclops.cyclopscore.component;

import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 */
public class DataComponentFluidContentConfig extends DataComponentConfigCommon<SimpleFluidContent, ModBase<?>> {
    public DataComponentFluidContentConfig() {
        super(CyclopsCore._instance, "fluid_content", builder -> builder
                .persistent(SimpleFluidContent.CODEC)
                .networkSynchronized(SimpleFluidContent.STREAM_CODEC));
    }
}
