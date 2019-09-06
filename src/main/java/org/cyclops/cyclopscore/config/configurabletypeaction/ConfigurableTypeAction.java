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
     * Logic to register the eConfig target when the config is being loaded during mod construction.
     * @param eConfig The config to be registered.
     */
    public void onRegisterModInit(C eConfig) {

    }

    /**
     * Logic to register things before Forge registries are being filled.
     * @param eConfig The config to be registered.
     */
    public void onRegisterForge(C eConfig) {

    }

    /**
     * Logic to register the eConfig target when the config is being loaded during the
     * {@link net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent}.
     * @param eConfig The config to be registered.
     */
    public void onRegisterSetup(C eConfig) {

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
