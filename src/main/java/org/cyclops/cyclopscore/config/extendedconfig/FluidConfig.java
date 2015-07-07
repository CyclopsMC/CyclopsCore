package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraftforge.fluids.Fluid;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for fluids.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class FluidConfig extends ExtendedConfig<FluidConfig> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public FluidConfig(ModBase mod, boolean enabled, String namedId,
            String comment, Class<? extends Fluid> element) {
        super(mod, enabled, namedId, comment, element);
    }
    
    @Override
	public String getUnlocalizedName() {
		return "fluids." + getMod().getModId() + "." + getNamedId();
	}
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.FLUID;
	}
    
    @Override
    public boolean isDisableable() {
        return false;
    }

}
