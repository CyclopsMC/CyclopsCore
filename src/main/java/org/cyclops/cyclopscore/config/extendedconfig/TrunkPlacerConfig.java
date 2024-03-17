package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for trunk placer types.
 * @author rubensworks
 * @see ExtendedConfig
 */
public class TrunkPlacerConfig<T extends TrunkPlacer> extends ExtendedConfigForge<TrunkPlacerConfig<T>, TrunkPlacerType<T>> {

    public TrunkPlacerConfig(ModBase mod, String namedId, Function<TrunkPlacerConfig<T>, Codec<T>> codec) {
        super(mod, namedId, (eConfig) -> new TrunkPlacerType<>(codec.apply(eConfig)));
    }

    @Override
    public String getTranslationKey() {
        return "trunkplacer." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.TRUNK_PLACER;
    }

    @Override
    public Registry<? super TrunkPlacerType<T>> getRegistry() {
        return BuiltInRegistries.TRUNK_PLACER_TYPE;
    }
}
