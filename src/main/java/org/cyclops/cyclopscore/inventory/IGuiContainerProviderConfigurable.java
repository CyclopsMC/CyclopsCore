package org.cyclops.cyclopscore.inventory;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Interface for configurables providing gui-containers.
 * @author rubensworks
 *
 */
public interface IGuiContainerProviderConfigurable extends IGuiContainerProvider {

    /**
     * @return The configurable config.
     */
    public ExtendedConfig<?, ?> getConfig();
	
}
