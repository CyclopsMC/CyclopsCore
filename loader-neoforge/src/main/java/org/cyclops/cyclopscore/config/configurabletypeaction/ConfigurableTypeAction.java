package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 */
@Deprecated // TODO: rm in next major
public class ConfigurableTypeAction<C extends ExtendedConfig<C, I>, I> extends ConfigurableTypeActionCommon<C, I, ModBase<?>> {
}
