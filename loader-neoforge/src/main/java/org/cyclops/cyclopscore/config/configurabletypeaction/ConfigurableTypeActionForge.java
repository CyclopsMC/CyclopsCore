package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.core.Registry;
import org.cyclops.cyclopscore.config.ConfigHandlerNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigForge;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author rubensworks
 */
@Deprecated // TODO: rm in next major
public class ConfigurableTypeActionForge<C extends ExtendedConfigForge<C, I>, I> extends ConfigurableTypeAction<C, I> {

    @Override
    public void onRegisterForgeFilled(C eConfig) {
        register(eConfig);
    }

    /**
     * Register the forge registry entry inside the given config.
     * @param config The corresponding config.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     */
    public static <C extends ExtendedConfigForge<C, I>, I> void register(C config) {
        register(config, () -> {
            config.onForgeRegistered();
            return null;
        });
    }

    /**
     * Register the forge registry entry inside the given config.
     * @param config The corresponding config.
     * @param callback A callback that will be called when the entry is registered.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     */
    public static <C extends ExtendedConfigForge<C, I>, I> void register(C config, @Nullable Callable<?> callback) {
        register(Objects.requireNonNull(config.getRegistry(),
                "Tried registering a config for which no registry exists: " + config.getNamedId()), config, callback);
    }

    /**
     * Register the forge registry entry inside the given config.
     * @param registry The registry.
     * @param config The corresponding config.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     */
    public static <C extends ExtendedConfigForge<C, I>, I> void register(Registry<? super I> registry, C config) {
        register(registry, config, null);
    }

    /**
     * Register the forge registry entry inside the given config.
     * @param registry The registry.
     * @param config The config.
     * @param callback A callback that will be called when the entry is registered.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     */
    public static <C extends ExtendedConfigForge<C, I>, I> void register(Registry<? super I> registry, C config, @Nullable Callable<?> callback) {
        ((ConfigHandlerNeoForge) config.getMod().getConfigHandler()).registerToRegistry(registry, config, callback);
    }

}
