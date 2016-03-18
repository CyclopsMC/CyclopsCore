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
        super(new BiomeProperties(L10NHelpers.localize(eConfig.getUnlocalizedName())));
        BiomeGenBase.registerBiome(eConfig.getId(), eConfig.getMod() + ":" + eConfig.getNamedId(), this);
        this.setConfig(eConfig);
        
    }
    
    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = (BiomeConfig)eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

}
