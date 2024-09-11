package org.cyclops.cyclopscore.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 */
public class DataComponentEnergyStorageConfig extends DataComponentConfigCommon<Integer, IModBase> {
    public DataComponentEnergyStorageConfig(IModBase mod) {
        super(mod, "energy_storage", builder -> builder
                .persistent(Codec.INT)
                .networkSynchronized(ByteBufCodecs.INT));
    }
}
