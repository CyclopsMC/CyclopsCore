package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigRegistry;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * The action used for {@link ExtendedConfigRegistry}.
 * @author rubensworks
 * @param <M> The mod type
 * @see ConfigurableTypeActionCommon
 */
public class ConfigurableTypeActionRegistry<C extends ExtendedConfigRegistry<C, I, M>, I, M extends IModBase>
        extends ConfigurableTypeActionCommon<C, I, M> {

    @Override
    public void onRegisterForgeFilled(C eConfig) {
        register(eConfig);
    }
}
