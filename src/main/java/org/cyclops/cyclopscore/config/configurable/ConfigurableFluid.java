package org.cyclops.cyclopscore.config.configurable;

import net.minecraftforge.fluids.Fluid;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;

/**
 * Fluid that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableFluid extends Fluid implements IConfigurable<FluidConfig>{
    
    protected FluidConfig eConfig = null;
    
    /**
     * Make a new fluid instance.
     * @param eConfig Config for this blockState.
     */
    protected ConfigurableFluid(ExtendedConfig<FluidConfig> eConfig) {
        super(eConfig.getNamedId(), ((FluidConfig)eConfig).getIconLocationStill(), ((FluidConfig)eConfig).getIconLocationFlow());
        this.setConfig((FluidConfig)eConfig); // TODO change eConfig to just be a FluidConfig
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    private void setConfig(FluidConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public FluidConfig getConfig() {
        return eConfig;
    }

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

}
