package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

/**
 * The action used for {@link CapabilityConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class CapabilityAction<T> extends ConfigurableTypeAction<CapabilityConfig<T>, T> {

    public CapabilityAction() {}

    @Override
    public void onRegisterForge(CapabilityConfig<T> config) {
        super.onRegisterForge(config);
        config.getRegistrar().apply(config);
    }
}
