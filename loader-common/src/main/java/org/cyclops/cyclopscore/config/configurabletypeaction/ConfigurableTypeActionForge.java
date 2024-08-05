package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigForge;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * The action used for {@link ExtendedConfigForge}.
 * @author rubensworks
 * @param <M> The mod type
 * @see ConfigurableTypeAction
 */
public class ConfigurableTypeActionForge<C extends ExtendedConfigForge<C, I, M>, I, M extends IModBase>
        extends ConfigurableTypeAction<C, I, M> { // TODO: rename in next major to ConfigurableTypeActionCommon

    @Override
    public void onRegisterForgeFilled(C eConfig) {
        register(eConfig);
    }
}
