package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeAction;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * @author rubensworks
 */
@Deprecated // TODO: rm in next major
public class ConfigurableType extends ConfigurableTypeCommon {
    /**
     * Make a new instance.
     *
     * @param uniqueInstance If this type has a unique instance for each config.
     * @param configClass    The config class.
     * @param action         The action instance that helps with saving of the config and optional instance.
     * @param category       The category in which the configs should be saved.
     */
    public ConfigurableType(boolean uniqueInstance, Class<? extends ExtendedConfig> configClass, ConfigurableTypeAction action, String category) {
        super(uniqueInstance, configClass, action, category);
    }
}
