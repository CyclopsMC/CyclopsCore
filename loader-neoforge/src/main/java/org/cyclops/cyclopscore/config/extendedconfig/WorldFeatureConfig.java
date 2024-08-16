package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for world features.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class WorldFeatureConfig extends ExtendedConfigForge<WorldFeatureConfig, Feature<?>> {
    public WorldFeatureConfig(ModBase<?> mod, String namedId, Function<WorldFeatureConfig, ? extends Feature<?>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "features." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.D_WORLD_FEATURE;
    }

    @Override
    public Registry<? super Feature<?>> getRegistry() {
        return BuiltInRegistries.FEATURE;
    }
}
