package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
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
        if (e instanceof BlockConfig || e instanceof ItemConfig) {
            getDictionary().put(e.getNamedId(), e);
        }
        super.addToConfigDictionary(e);
    }

    @Override
    public void addToConfigDictionary(ExtendedConfigCommon<?, ?, ?> e) {
        if (e instanceof BlockConfig || e instanceof ItemConfig) {
            getDictionary().put(e.getNamedId(), e);
        }
        super.addToConfigDictionary(e);
    }
}
