package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for biomes.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BiomeConfig extends ExtendedConfigForge<BiomeConfig, Biome>{

    public BiomeConfig(ModBase mod, String namedId, Function<BiomeConfig, ? extends Biome> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
    
    @Override
	public String getTranslationKey() {
		return "biomes." + getMod().getModId() + "." + getNamedId();
	}
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.BIOME;
	}

    @Override
    public IForgeRegistry<Biome> getRegistry() {
        return ForgeRegistries.BIOMES;
    }
}
