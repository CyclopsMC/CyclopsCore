package org.cyclops.cyclopscore.component;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 */
public class DataComponentCapacityConfig extends DataComponentConfigCommon<Integer, ModBase<?>> {
    public DataComponentCapacityConfig() {
        super(CyclopsCore._instance, "capacity", builder -> builder
                .persistent(ExtraCodecs.POSITIVE_INT)
                .networkSynchronized(ByteBufCodecs.VAR_INT));
    }
}
