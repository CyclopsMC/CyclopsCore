package org.cyclops.cyclopscore.config.configurable;

import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;


/**
 * Interface for all elements that are configurable.
 * Each type has one unique {@link ExtendedConfig} that must be registered inside the {@link ConfigHandler}.
 * @author rubensworks
 *
 */
public interface IConfigurable {

    public ExtendedConfig<?> getConfig();

}
