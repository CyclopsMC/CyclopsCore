package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfigCommon;

/**
 * The action used for {@link CapabilityConfigCommon}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
@Deprecated // TODO: rm in next major
public class CapabilityActionOld<T> extends ConfigurableTypeAction<CapabilityConfig<T>, T> {

    public CapabilityActionOld() {}

    @Override
    public void onRegisterForge(CapabilityConfig<T> config) {
        super.onRegisterForge(config);
        config.getRegistrar().apply(config);
    }
}
