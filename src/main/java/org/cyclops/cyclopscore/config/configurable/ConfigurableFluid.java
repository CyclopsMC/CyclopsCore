package org.cyclops.cyclopscore.config.configurable;

import net.minecraftforge.fluids.Fluid;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;

/**
 * Fluid that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableFluid extends Fluid implements IConfigurable{
    
    protected ExtendedConfig<FluidConfig, Fluid> eConfig = null;
    
    /**
     * Make a new fluid instance.
     * @param eConfig Config for this blockState.
     */
    protected ConfigurableFluid(ExtendedConfig<FluidConfig, Fluid> eConfig) {
        super(eConfig.getNamedId(), eConfig.downCast().getIconLocationStill(), eConfig.downCast().getIconLocationFlow());
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    private void setConfig(ExtendedConfig<FluidConfig, Fluid> eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<FluidConfig, Fluid> getConfig() {
        return eConfig;
    }

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

}
