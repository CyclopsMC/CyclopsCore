package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlock extends Block implements IConfigurable {
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    protected int pass = 0;
    protected boolean isInventoryBlock = false;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlock(ExtendedConfig eConfig, Material material) {
        super(material);
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    private void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

}
