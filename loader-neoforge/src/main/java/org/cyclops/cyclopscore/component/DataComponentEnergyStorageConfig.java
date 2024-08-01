package org.cyclops.cyclopscore.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;

/**
 * @author rubensworks
 */
public class DataComponentEnergyStorageConfig extends DataComponentConfig<Integer> {
    public DataComponentEnergyStorageConfig() {
        super(CyclopsCore._instance, "energy_storage", builder -> builder
                .persistent(Codec.INT)
                .networkSynchronized(ByteBufCodecs.INT));
    }
}
