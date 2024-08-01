package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigForge;

/**
 * The action used for {@link ExtendedConfigForge}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ConfigurableTypeActionForge<C extends ExtendedConfigForge<C, I>, I>
        extends ConfigurableTypeAction<C, I> {

    @Override
    public void onRegisterForgeFilled(C eConfig) {
        register(eConfig);
    }
}
