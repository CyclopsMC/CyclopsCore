package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

/**
 * The action used for {@link CapabilityConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class CapabilityAction<T> extends ConfigurableTypeAction<CapabilityConfig<T>> {

    @Override
    public void preRun(CapabilityConfig<T> eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(), eConfig.isEnabled());
        property.setRequiresMcRestart(true);
        property.setComment(eConfig.getComment());
        
        if(startup) {
	        eConfig.setEnabled(property.getBoolean(false));
        }
    }

    @Override
    public void postRun(CapabilityConfig<T> eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register capability
        CapabilityManager.INSTANCE.register(eConfig.getType(), eConfig.getStorage(), eConfig.getImplementation());
    }

}
