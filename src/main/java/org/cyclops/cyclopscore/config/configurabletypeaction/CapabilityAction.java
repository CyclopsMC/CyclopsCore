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
    public void onRegisterModInit(CapabilityConfig<T> eConfig) {
        // CapabilityManager.INSTANCE is not properly initialized at this point, so we have to register later.
    }

    @Override
    public void onRegisterSetup(CapabilityConfig<T> eConfig) {
        CapabilityManager.INSTANCE.register(eConfig.getType(), eConfig.getStorage(), eConfig.getFactory());
    }
}
