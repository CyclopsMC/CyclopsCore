package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigForge;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * An action that is used to register Configurables.
 * Used inside of {@link ConfigHandler}.
 * @author rubensworks
 * @param <C> The subclass of ExtendedConfig.
 * @param <I> The instance corresponding to this config.
 * @see ConfigHandler
 */
public abstract class ConfigurableTypeAction<C extends ExtendedConfig<C, I>, I> {
    
    /**
     * The common run method for all the subtypes of {@link ConfigurableTypeAction}.
     * @param eConfig The config to be registered.
     * @param configBuilder The configuration builder.
     */
    public void onConfigInit(C eConfig, ForgeConfigSpec.Builder configBuilder) {
        if(eConfig.isDisableable()) {
            buildConfigEnabled(eConfig, configBuilder);
        }
    }

    /**
     * Logic to register the eConfig target when the config is being loaded.
     * @param eConfig The config to be registered.
     */
    public abstract void onRegister(C eConfig);
    
    public void onSkipRegistration(C eConfig) {
        eConfig.getMod().log(Level.TRACE, "Skipped registering " + eConfig.getNamedId());
    }
    
    /**
     * Logic that constructs the config options.
     * @param eConfig The config to be registered.
     * @param configBuilder The configuration builder.
     */
    public void buildConfigEnabled(C eConfig, ForgeConfigSpec.Builder configBuilder) {
        configBuilder.push(eConfig.getConfigurableType().getCategory());

        // Construct property for enabling the configurable
        if (eConfig.getComment() != null) {
            configBuilder = configBuilder.comment(eConfig.getComment());
        }
        ForgeConfigSpec.BooleanValue configProperty = configBuilder
                .translation(eConfig.getFullTranslationKey())
                .worldRestart()
                .define(eConfig.getNamedId(), eConfig.isEnabledDefault());
        eConfig.setPropertyEnabled(configProperty);

        configBuilder.pop();
    }

    /**
     * Register the {@link IForgeRegistryEntry}.
     * @param instance The instance.
     * @param config The corresponding config.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     */
    public static <C extends ExtendedConfigForge<C, I>, I extends IForgeRegistryEntry<I>> void register(I instance, C config) {
        register(instance, config, () -> {
            config.onForgeRegistered();
            return null;
        });
    }

    /**
     * Register the {@link IForgeRegistryEntry}.
     * @param instance The instance.
     * @param config The corresponding config.
     * @param callback A callback that will be called when the entry is registered.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     */
    public static <C extends ExtendedConfigForge<C, I>, I extends IForgeRegistryEntry<I>> void register(I instance, C config, @Nullable Callable<?> callback) {
        register(Objects.requireNonNull(config.getRegistry(),
                "Tried registering a config for which no registry exists: " + config.getNamedId()), instance, config, callback);
    }

    /**
     * Register the {@link IForgeRegistryEntry}.
     * @param registry The registry.
     * @param instance The instance.
     * @param config The corresponding config.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     */
    public static <C extends ExtendedConfigForge<C, I>, I extends IForgeRegistryEntry<I>> void register(IForgeRegistry<I> registry, I instance, C config) {
        register(registry, instance, config, null);
    }

    /**
     * Register the {@link IForgeRegistryEntry}.
     * @param registry The registry.
     * @param instance The instance.
     * @param config The corresponding config.
     * @param callback A callback that will be called when the entry is registered.
     * @param <C> The subclass of ExtendedConfig.
     * @param <I> The instance corresponding to this config.
     */
    public static <C extends ExtendedConfigForge<C, I>, I extends IForgeRegistryEntry<I>> void register(IForgeRegistry<? super I> registry, I instance, C config, @Nullable Callable<?> callback) {
        if (instance.getRegistryName() == null) {
            instance.setRegistryName(new ResourceLocation(config.getMod().getModId(), config.getNamedId()));
        }
        config.getMod().getConfigHandler().registerToRegistry(registry, instance, callback);
    }
}
