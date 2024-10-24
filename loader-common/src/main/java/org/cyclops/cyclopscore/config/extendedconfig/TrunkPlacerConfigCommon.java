package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for trunk placer types.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public class TrunkPlacerConfigCommon<T extends TrunkPlacer, M extends IModBase> extends ExtendedConfigRegistry<TrunkPlacerConfigCommon<T, M>, TrunkPlacerType<T>, M> {

    public TrunkPlacerConfigCommon(M mod, String namedId, Function<TrunkPlacerConfigCommon<T, M>, MapCodec<T>> codec) {
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
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.TRUNK_PLACER;
    }

    @Override
    public Registry<? super TrunkPlacerType<T>> getRegistry() {
        return BuiltInRegistries.TRUNK_PLACER_TYPE;
    }
}
