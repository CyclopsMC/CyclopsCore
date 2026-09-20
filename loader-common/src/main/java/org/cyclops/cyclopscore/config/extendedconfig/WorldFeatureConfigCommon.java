package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for world feature types.
 * @author rubensworks
 * @param <F> The feature type
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class WorldFeatureConfigCommon<F extends Feature, M extends IModBase> extends ExtendedConfigRegistry<WorldFeatureConfigCommon<F, M>, MapCodec<F>, M> {

    public WorldFeatureConfigCommon(M mod, String namedId, Function<WorldFeatureConfigCommon<F, M>, MapCodec<F>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "features." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.WORLD_FEATURE;
    }

    @Override
    public Registry<? super MapCodec<F>> getRegistry() {
        return BuiltInRegistries.FEATURE_TYPE;
    }
}
