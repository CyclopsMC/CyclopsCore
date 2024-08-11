package org.cyclops.cyclopscore.helper;

import net.minecraft.core.Registry;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigForge;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

/**
 * @author rubensworks
 */
public class ConfigHandlerFabric extends ConfigHandler {
    public ConfigHandlerFabric(IModBase mod) {
        super(mod);
    }

    @Override
    public <V> void registerToRegistry(Registry<? super V> registry, ExtendedConfigForge<?, V, ?> config, @Nullable Callable<?> callback) {
        Registry.register(registry, getConfigId(config), config.getInstance());
        if (callback != null) {
            try {
                callback.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
