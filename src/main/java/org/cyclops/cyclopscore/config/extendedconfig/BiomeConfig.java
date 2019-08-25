package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
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

    public BiomeConfig(ModBase mod, boolean enabledDefault, String namedId,
                       String comment, Function<BiomeConfig, ? extends Biome> elementConstructor) {
        super(mod, enabledDefault, namedId, comment, elementConstructor);
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

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        BiomeDictionary.makeBestGuess(getInstance());
    }
}
