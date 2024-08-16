package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for trunk placer types.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public class TrunkPlacerConfig<T extends TrunkPlacer> extends TrunkPlacerConfigCommon<T, ModBase<?>> {
    public TrunkPlacerConfig(ModBase<?> mod, String namedId, Function<TrunkPlacerConfigCommon<T, ModBase<?>>, MapCodec<T>> codec) {
        super(mod, namedId, codec);
    }
}
