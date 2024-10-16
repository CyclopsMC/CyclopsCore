package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for world features.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class WorldFeatureConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<WorldFeatureConfigCommon<M>, Feature<?>, M> {

    public WorldFeatureConfigCommon(M mod, String namedId, Function<WorldFeatureConfigCommon<M>, ? extends Feature<?>> elementConstructor) {
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
    public Registry<? super Feature<?>> getRegistry() {
        return BuiltInRegistries.FEATURE;
    }
}
