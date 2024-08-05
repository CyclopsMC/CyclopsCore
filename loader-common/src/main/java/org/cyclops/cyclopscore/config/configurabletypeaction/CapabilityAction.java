package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * The action used for {@link CapabilityConfigCommon}.
 * @author rubensworks
 * @param <M> The mod type
 * @see ConfigurableTypeAction
 */
public class CapabilityAction<T, M extends IModBase> extends ConfigurableTypeAction<CapabilityConfigCommon<T, M>, T, M> {

    public CapabilityAction() {}

    @Override
    public void onRegisterForge(CapabilityConfigCommon<T, M> config) {
        super.onRegisterForge(config);
        config.getRegistrar().apply(config);
    }
}
