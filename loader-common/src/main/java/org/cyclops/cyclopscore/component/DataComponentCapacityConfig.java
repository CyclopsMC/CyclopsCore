package org.cyclops.cyclopscore.component;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 */
public class DataComponentCapacityConfig extends DataComponentConfigCommon<Integer, IModBase> {
    public DataComponentCapacityConfig(IModBase mod) {
        super(mod, "capacity", builder -> builder
                .persistent(ExtraCodecs.POSITIVE_INT)
                .networkSynchronized(ByteBufCodecs.VAR_INT));
    }
}
