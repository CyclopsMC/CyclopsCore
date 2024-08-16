package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigRegistry;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 */
@Deprecated // TODO: rm in next major
public class ConfigurableTypeActionForge<C extends ExtendedConfigRegistry<C, I, ModBase<?>>, I> extends ConfigurableTypeActionRegistry<C, I, ModBase<?>> {
}
