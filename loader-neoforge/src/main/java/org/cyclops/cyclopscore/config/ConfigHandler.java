package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 */
@Deprecated // TODO: rm in next major
public abstract class ConfigHandler extends ConfigHandlerCommon {
    public ConfigHandler(ModBase<?> mod) {
        super(mod);
    }

    public boolean addConfigurable(ExtendedConfig<?, ?> e) {
        return super.addConfigurable(e);
    }

    public void addToConfigDictionary(ExtendedConfig<?, ?> e) {
        super.addToConfigDictionary(e);
    }
}
