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
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * Make a new fluid instance.
     * @param eConfig Config for this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    protected ConfigurableFluid(ExtendedConfig<FluidConfig> eConfig) {
        super(eConfig.getNamedId(), eConfig.downCast().getIconLocationStill(), eConfig.downCast().getIconLocationFlow());
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

}
