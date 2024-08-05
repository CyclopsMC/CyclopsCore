package org.cyclops.cyclopscore.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 */
public class DataComponentEnergyStorageConfig extends DataComponentConfigCommon<Integer, ModBase<?>> {
    public DataComponentEnergyStorageConfig() {
        super(CyclopsCore._instance, "energy_storage", builder -> builder
                .persistent(Codec.INT)
                .networkSynchronized(ByteBufCodecs.INT));
    }
}
