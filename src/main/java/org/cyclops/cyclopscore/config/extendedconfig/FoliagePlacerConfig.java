package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for foliage placer types.
 * @author rubensworks
 * @see ExtendedConfig
 */
public class FoliagePlacerConfig<T extends FoliagePlacer> extends ExtendedConfigForge<FoliagePlacerConfig<T>, FoliagePlacerType<T>> {

    public FoliagePlacerConfig(ModBase mod, String namedId, Function<FoliagePlacerConfig<T>, MapCodec<T>> codec) {
        super(mod, namedId, (eConfig) ->  new FoliagePlacerType<>(codec.apply(eConfig)));
    }

    @Override
    public String getTranslationKey() {
        return "foliageplacer." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.FOLIAGE_PLACER;
    }

    @Override
    public Registry<? super FoliagePlacerType<T>> getRegistry() {
        return BuiltInRegistries.FOLIAGE_PLACER_TYPE;
    }
}
