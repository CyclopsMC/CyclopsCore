package org.cyclops.cyclopscore.component;

import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;

/**
 * @author rubensworks
 */
public class DataComponentFluidContentConfig extends DataComponentConfig<SimpleFluidContent> {
    public DataComponentFluidContentConfig() {
        super(CyclopsCore._instance, "fluid_content", builder -> builder
                .persistent(SimpleFluidContent.CODEC)
                .networkSynchronized(SimpleFluidContent.STREAM_CODEC));
    }
}
