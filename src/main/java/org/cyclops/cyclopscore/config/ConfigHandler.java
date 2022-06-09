package org.cyclops.cyclopscore.config;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import lombok.Data;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeAction;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigForge;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Create config file and register items and blocks from the given ExtendedConfigs.
 * @author rubensworks
 *
 */
@SuppressWarnings("rawtypes")
@Data
public class ConfigHandler {

    private final ModBase mod;
    private final LinkedHashSet<ExtendedConfig<?, ?>> configurables = new LinkedHashSet<>();
    private final Map<String, ExtendedConfig<?, ?>> configDictionary = Maps.newHashMap();
    private final Set<String> categories = Sets.newHashSet();
    private final Map<String, ConfigurablePropertyData> commandableProperties = Maps.newHashMap();
    private final Multimap<String, Pair<ExtendedConfigForge<?, ?>, Callable<?>>> registryEntriesHolder = Multimaps.newListMultimap(Maps.<String, Collection<Pair<ExtendedConfigForge<?, ?>, Callable<?>>>>newHashMap(), new Supplier<List<Pair<ExtendedConfigForge<?, ?>, Callable<?>>>>() {
        // Compiler complains when this is replaced with a lambda :-(
        @Override
        public List<Pair<ExtendedConfigForge<?, ?>, Callable<?>>> get() {
            return Lists.newArrayList();
        }
    });
    private boolean registryEventPassed = false;

    public ConfigHandler(ModBase mod) {
        this.mod = mod;
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    /**
     * Initialize the configs by running builders through all relevant parts.
     * @param configInitializers A collection of additional initializers to run the config builders through.
     */
    public void initialize(Collection<IConfigInitializer> configInitializers) {
        Map<ModConfig.Type, ForgeConfigSpec.Builder> configBuilders = new EnumMap<>(ModConfig.Type.class);

        // Pass config builder to all configurables
        for (ExtendedConfig<?, ?> eConfig : this.configurables) {
            ForgeConfigSpec.Builder configBuilder = configBuilders.get(ModConfig.Type.COMMON);
            if (configBuilder == null) {
                configBuilder = new ForgeConfigSpec.Builder();
                configBuilders.put(ModConfig.Type.COMMON, configBuilder);
            }
            addCategory(eConfig.getConfigurableType().getCategory());

            // Save additional properties
            for (ConfigurablePropertyData configProperty : eConfig.configProperties.values()) {
                ForgeConfigSpec.Builder configBuilderProperty = configBuilders.get(configProperty.getConfigLocation());
                if (configBuilderProperty == null) {
                    configBuilderProperty = new ForgeConfigSpec.Builder();
                    configBuilders.put(configProperty.getConfigLocation(), configBuilderProperty);
                }
                categories.add(configProperty.getCategory());
                configProperty.onConfigInit(configBuilder);
                if (configProperty.isCommandable()) {
                    commandableProperties.put(configProperty.getName(), configProperty);
                }
            }
        }

        // Handle all config initializers
        for (IConfigInitializer configInitializer : configInitializers) {
            configInitializer.initializeConfig(configBuilders);
        }

        // Finalize config builders to config specs, and register them
        for (Map.Entry<ModConfig.Type, ForgeConfigSpec.Builder> entry : configBuilders.entrySet()) {
            ModLoadingContext.get().registerConfig(entry.getKey(), entry.getValue().build());
        }
    }

    @SubscribeEvent
    public void onLoad(ModConfigEvent.Loading configEvent) {
        this.mod.log(Level.TRACE, "Load config");
        syncProcessedConfigs(configEvent.getConfig(), false);
    }

    @SubscribeEvent
    public void onReload(ModConfigEvent.Reloading configEvent) {
        this.mod.log(Level.TRACE, "Reload config");
        syncProcessedConfigs(configEvent.getConfig(), true);
    }

    public boolean addConfigurable(ExtendedConfig<?, ?> e) {
        addToConfigDictionary(e);
        return configurables.add(e);
    }

    public void addToConfigDictionary(ExtendedConfig<?, ?> e) {
        if (e instanceof BlockConfig || e instanceof ItemConfig || e instanceof FluidConfig) {
            configDictionary.put(e.getNamedId(), e);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs to read/write the config and register the given elements
     * This also sets the config of this instance.
     * This is called during mod construction.
     */
    public void loadModInit() {
        for (ExtendedConfig<?, ?> eConfig : this.configurables) {
            mod.log(Level.TRACE, "Registering " + eConfig.getNamedId());
            eConfig.getConfigurableType().getConfigurableTypeAction().onRegisterModInit(eConfig);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs and call {@link ConfigurableTypeAction#onRegisterForge(ExtendedConfig)}
     * during the {@link net.minecraftforge.registries.NewRegistryEvent} event.
     */
    public void loadForgeRegistries() {
        for (ExtendedConfig<?, ?> eConfig : this.configurables) {
            eConfig.getConfigurableType().getConfigurableTypeAction().onRegisterForge(eConfig);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs and call {@link ConfigurableTypeAction#onRegisterForgeFilled(ExtendedConfig)}
     * during the {@link RegisterEvent} event.
     */
    public void loadForgeRegistriesFilled() {
        for (ExtendedConfig<?, ?> eConfig : this.configurables) {
            eConfig.getConfigurableType().getConfigurableTypeAction().onRegisterForgeFilled(eConfig);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs and call {@link ConfigurableTypeAction#onRegisterSetup(ExtendedConfig)}
     * during the {@link net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent}.
     */
    public void loadSetup() {
        for (ExtendedConfig<?, ?> eConfig : this.configurables) {
            eConfig.getConfigurableType().getConfigurableTypeAction().onRegisterSetup(eConfig);
            eConfig.onRegistered();
        }
    }

    /**
     * Add a config category.
     * @param category The category to add.
     */
    public void addCategory(String category) {
        categories.add(category);
    }

    /**
     * Sync the config values that were already loaded.
     * This will update the values in-game and in the config file.
     * @param config The mod config that is being loaded.
     * @param reload If we are reloading, otherwise this is an initial load.
     */
    @SuppressWarnings("unchecked")
    public void syncProcessedConfigs(ModConfig config, boolean reload) {
        for(ExtendedConfig<?, ?> eConfig : this.configurables) {
            // Re-save additional properties
            for(ConfigurablePropertyData configProperty : eConfig.configProperties.values()) {
                if (configProperty.getConfigLocation() == config.getType()) {
                    configProperty.saveToField();
                    eConfig.onConfigPropertyReload(configProperty, reload);
                }
            }
        }
    }

    /**
     * Get the map of config nameid to config.
     * @return The dictionary.
     */
    public Map<String, ExtendedConfig<?, ?>> getDictionary() {
        return configDictionary;
    }

    /**
     * Register the given entry to the given registry.
     * This method will safely wait until the correct registry event for registering the entry.
     * @param registry The registry.
     * @param config The config.
     * @param callback A callback that will be called when the entry is registered.
     * @param <V> The entry type.
     */
    public <V> void registerToRegistry(IForgeRegistry<? super V> registry,
                                       ExtendedConfigForge<?, V> config,
                                       @Nullable Callable<?> callback) {
        if (this.registryEventPassed) {
            throw new IllegalStateException(String.format("Tried registering %s after its registration event.",
                    config.getNamedId()));
        }
        registryEntriesHolder.put(registry.getRegistryKey().toString(), Pair.of(config, callback));
    }

    @SubscribeEvent
    public void onRegistryEvent(RegisterEvent event) {
        if (event.getForgeRegistry() != null) {
            this.registryEventPassed = true;
            IForgeRegistry registry = event.getForgeRegistry();
            registryEntriesHolder.get(registry.getRegistryKey().toString()).forEach((pair) -> {
                ExtendedConfigForge<?, ?> config = pair.getLeft();
                registry.register(new ResourceLocation(config.getMod().getModId(), config.getNamedId()), config.getInstance());
                try {
                    if (pair.getRight() != null) {
                        pair.getRight().call();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
