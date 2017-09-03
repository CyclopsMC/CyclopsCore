package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBiome;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for biomes.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BiomeConfig extends ExtendedConfig<BiomeConfig, Biome>{

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param defaultId The default ID for the configurable.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public BiomeConfig(ModBase mod, int defaultId, String namedId,
            String comment, Class<? extends Biome> element) {
        super(mod, defaultId > 0, namedId, comment, element);
    }
    
    @Override
	public String getUnlocalizedName() {
		return "biomes." + getMod().getModId() + "." + getNamedId();
	}
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.BIOME;
	}

	/**
     * Get the biome configurable
     * @return The biome.
     */
    public ConfigurableBiome getBiome() {
        return (ConfigurableBiome) this.getSubInstance();
    }
    
    /**
     * Register the biome instance into the biome dictionary.
     * @see BiomeDictionary
     */
    public void registerBiomeDictionary() {
        BiomeDictionary.makeBestGuess(getBiome());
    }

    @Override
    public IForgeRegistry<?> getRegistry() {
        return ForgeRegistries.BIOMES;
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        registerBiomeDictionary();
    }
}
