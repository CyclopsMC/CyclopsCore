package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.common.capabilities.CapabilityManager;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

/**
 * The action used for {@link CapabilityConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class CapabilityAction<T> extends ConfigurableTypeAction<CapabilityConfig<T>, T> {
    @Override
    public void onRegisterForgeFilled(CapabilityConfig<T> eConfig) {
        // We have to call this here because the CapabilityManager is only initialized properly at this point (see ModLoader#gatherAndInitializeMods).
        CapabilityManager.INSTANCE.register(eConfig.getType(), eConfig.getStorage(), eConfig.getFactory());
    }
}
