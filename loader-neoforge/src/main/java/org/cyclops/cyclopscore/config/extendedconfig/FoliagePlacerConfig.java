package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for foliage placer types.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public class FoliagePlacerConfig<T extends FoliagePlacer> extends FoliagePlacerConfigCommon<T, ModBase<?>> {
    public FoliagePlacerConfig(ModBase<?> mod, String namedId, Function<FoliagePlacerConfigCommon<T, ModBase<?>>, MapCodec<T>> codec) {
        super(mod, namedId, codec);
    }
}
