package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for world features.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class WorldFeatureConfig extends ExtendedConfigForge<WorldFeatureConfig, Feature<?>>{

    public WorldFeatureConfig(ModBase mod, String namedId, Function<WorldFeatureConfig, ? extends Feature<?>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
	public String getTranslationKey() {
		return "features." + getMod().getModId() + "." + getNamedId();
	}

    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.WORLD_FEATURE;
	}

    @Override
    public IForgeRegistry<Feature<?>> getRegistry() {
        return ForgeRegistries.FEATURES;
    }

}
