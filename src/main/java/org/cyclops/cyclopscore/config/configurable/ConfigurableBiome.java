package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.world.biome.BiomeGenBase;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;

/**
 * A simple configurable for Biomes, will auto-register itself after construction.
 * @author rubensworks
 *
 */
public class ConfigurableBiome extends BiomeGenBase implements IConfigurable {

    protected BiomeConfig eConfig = null;
    
    /**
     * Make a new Biome instance
     * @param eConfig Config for this enchantment.
     */
    protected ConfigurableBiome(BiomeConfig eConfig) {
        super(constructProperties(eConfig));
        this.setConfig(eConfig);
    }

    protected ConfigurableBiome(BiomeProperties properties, BiomeConfig eConfig) {
        super(properties);
        this.setConfig(eConfig);
    }

    protected static Properties constructProperties(BiomeConfig eConfig) {
        return new Properties(L10NHelpers.localize(eConfig.getUnlocalizedName()));
    }
    
    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = (BiomeConfig)eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

    /**
     * An extension of {@link BiomeProperties} which has all its setters made public.
     */
    public static class Properties extends BiomeProperties {

        public Properties(String nameIn) {
            super(nameIn);
        }

        @Override
        public Properties setTemperature(float temperatureIn) {
            return (Properties) super.setTemperature(temperatureIn);
        }

        @Override
        public Properties setRainfall(float rainfallIn) {
            return (Properties) super.setRainfall(rainfallIn);
        }

        @Override
        public Properties setBaseHeight(float baseHeightIn) {
            return (Properties) super.setBaseHeight(baseHeightIn);
        }

        @Override
        public Properties setHeightVariation(float heightVariationIn) {
            return (Properties) super.setHeightVariation(heightVariationIn);
        }

        @Override
        public Properties setRainDisabled() {
            return (Properties) super.setRainDisabled();
        }

        @Override
        public Properties setSnowEnabled() {
            return (Properties) super.setSnowEnabled();
        }

        @Override
        public Properties setWaterColor(int waterColorIn) {
            return (Properties) super.setWaterColor(waterColorIn);
        }

        @Override
        public Properties setBaseBiome(String nameIn) {
            return (Properties) super.setBaseBiome(nameIn);
        }
    }

}
