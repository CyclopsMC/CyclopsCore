package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.core.Registry;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigForge;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * An action that is used to register Configurables.
 * Used inside of {@link ConfigHandler}.
 * @author rubensworks
 * @param <C> The subclass of ExtendedConfig.
 * @param <I> The instance corresponding to this config.
 * @param <M> The mod type
 * @see ConfigHandler
 */
public abstract class ConfigurableTypeAction<C extends ExtendedConfig<C, I, M>, I, M extends IModBase> {

    /**
     * Logic to register the eConfig target when the config is being loaded during mod construction.
     * @param eConfig The config to be registered.
     */
    public void onRegisterModInit(C eConfig) {

    }

    /**
     * Logic to register things right after the Forge registries have been created.
     * @param eConfig The config to be registered.
     */
    public void onRegisterForge(C eConfig) { // TODO: rename in next major version

    }

    /**
     * Logic to register things before Forge registries are being filled.
     * @param eConfig The config to be registered.
     */
    public void onRegisterForgeFilled(C eConfig) { // TODO: rename in next major version

    }

    /**
     * Logic to register the eConfig target when the config is being loaded during the
     * net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent.
     * @param eConfig The config to be registered.
     */
    public void onRegisterSetup(C eConfig) {

    }

    /**
     * Register the forge registry entry inside the given config.
     * @param config The corresponding config.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     * @param <M> The mod type
     */
    public static <C extends ExtendedConfigForge<C, I, M>, I, M extends IModBase> void register(C config) {
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
     * @param <M> The mod type
     */
    public static <C extends ExtendedConfigForge<C, I, M>, I, M extends IModBase> void register(C config, @Nullable Callable<?> callback) {
        register(Objects.requireNonNull(config.getRegistry(),
                "Tried registering a config for which no registry exists: " + config.getNamedId()), config, callback);
    }

    /**
     * Register the forge registry entry inside the given config.
     * @param registry The registry.
     * @param config The corresponding config.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     * @param <M> The mod type
     */
    public static <C extends ExtendedConfigForge<C, I, M>, I, M extends IModBase> void register(Registry<? super I> registry, C config) {
        register(registry, config, null);
    }

    /**
     * Register the forge registry entry inside the given config.
     * @param registry The registry.
     * @param config The config.
     * @param callback A callback that will be called when the entry is registered.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     * @param <M> The mod type
     */
    public static <C extends ExtendedConfigForge<C, I, M>, I, M extends IModBase> void register(Registry<? super I> registry, C config, @Nullable Callable<?> callback) {
        config.getMod().getConfigHandler().registerToRegistry(registry, config, callback);
    }
}
