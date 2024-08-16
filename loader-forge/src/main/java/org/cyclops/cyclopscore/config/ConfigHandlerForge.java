package org.cyclops.cyclopscore.config;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfigForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author rubensworks
 */
public class ConfigHandlerForge extends ConfigHandler {

    private final Multimap<String, Pair<ExtendedConfigRegistry<?, ?, ?>, Callable<?>>> registryEntriesHolder = Multimaps.newListMultimap(Maps.<String, Collection<Pair<ExtendedConfigRegistry<?, ?, ?>, Callable<?>>>>newHashMap(), new Supplier<List<Pair<ExtendedConfigRegistry<?, ?, ?>, Callable<?>>>>() {
        // Compiler complains when this is replaced with a lambda :-(
        @Override
        public List<Pair<ExtendedConfigRegistry<?, ?, ?>, Callable<?>>> get() {
            return Lists.newArrayList();
        }
    });
    private Set<String> registryEventPassed = Sets.newHashSet();

    public ConfigHandlerForge(ModBaseForge<?> mod) {
        super(mod);
        mod.getModEventBus().register(this);
    }

    public Multimap<String, Pair<ExtendedConfigRegistry<?, ?, ?>, Callable<?>>> getRegistryEntriesHolder() {
        return registryEntriesHolder;
    }

    public Set<String> getRegistryEventPassed() {
        return registryEventPassed;
    }

    @Override
    public <V> void registerToRegistry(Registry<? super V> registry,
                                       ExtendedConfigRegistry<?, V, ?> config,
                                       @Nullable Callable<?> callback) {
        if (this.registryEventPassed.contains(registry.key().toString())) {
            throw new IllegalStateException(String.format("Tried registering %s after its registration event.",
                    config.getNamedId()));
        }
        registryEntriesHolder.put(registry.key().toString(), Pair.of(config, callback));
    }

    @SubscribeEvent
    public void onLoad(ModConfigEvent.Loading configEvent) {
        this.getMod().log(Level.TRACE, "Load config");
        syncProcessedConfigs(configEvent.getConfig(), false);
    }

    @SubscribeEvent
    public void onReload(ModConfigEvent.Reloading configEvent) {
        this.getMod().log(Level.TRACE, "Reload config");
        syncProcessedConfigs(configEvent.getConfig(), true);
    }

    @SubscribeEvent
    public void onRegistryEvent(RegisterEvent event) {
        if (event.getForgeRegistry() != null) {
            this.registryEventPassed.add(event.getRegistryKey().toString());
            IForgeRegistry registry = event.getForgeRegistry();
            registryEntriesHolder.get(registry.getRegistryKey().toString()).forEach((pair) -> {
                ExtendedConfigRegistry<?, ?, ?> config = pair.getLeft();
                registry.register(getConfigId(config), config.getInstance());
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

    /**
     * Initialize the configs by running builders through all relevant parts.
     * @param configInitializers A collection of additional initializers to run the config builders through.
     */
    public void initialize(Collection<IConfigInitializer> configInitializers) {
        Map<ModConfig.Type, ForgeConfigSpec.Builder> configBuilders = new EnumMap<>(ModConfig.Type.class);

        // Pass config builder to all configurables
        for (ExtendedConfig<?, ?, ?> eConfig : getConfigurables()) {
            ForgeConfigSpec.Builder configBuilder = configBuilders.get(ModConfig.Type.COMMON);
            if (configBuilder == null) {
                configBuilder = new ForgeConfigSpec.Builder();
                configBuilders.put(ModConfig.Type.COMMON, configBuilder);
            }
            addCategory(eConfig.getConfigurableType().getCategory());

            // Save additional properties
            for (ConfigurablePropertyData configProperty : eConfig.configProperties.values()) {
                ForgeConfigSpec.Builder configBuilderProperty = configBuilders.get(modConfigLocationToType(configProperty.getConfigLocation()));
                if (configBuilderProperty == null) {
                    configBuilderProperty = new ForgeConfigSpec.Builder();
                    configBuilders.put(modConfigLocationToType(configProperty.getConfigLocation()), configBuilderProperty);
                }
                getCategories().add(configProperty.getCategory());
                this.onConfigPropertyInit(configProperty, configBuilder);
                if (configProperty.isCommandable()) {
                    getCommandableProperties().put(configProperty.getName(), configProperty);
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

    protected <T> void onConfigPropertyInit(ConfigurablePropertyData<T> configPropertyData, ForgeConfigSpec.Builder configBuilder) {
        configBuilder.push(configPropertyData.category);

        // Construct property for enabling the configurable
        if (configPropertyData.requiresWorldRestart) {
            configBuilder.worldRestart();
        }
        ForgeConfigSpec.ConfigValue<T> configProperty = configBuilder
                .comment(configPropertyData.comment)
                .translation(configPropertyData.getLanguageKey())
                .define(configPropertyData.name, configPropertyData.defaultValue);
        configPropertyData.setConfigProperty(configProperty);

        configBuilder.pop();
    }

    /**
     * Sync the config values that were already loaded.
     * This will update the values in-game and in the config file.
     * @param config The mod config that is being loaded.
     * @param reload If we are reloading, otherwise this is an initial load.
     */
    @SuppressWarnings("unchecked")
    public void syncProcessedConfigs(ModConfig config, boolean reload) {
        for(ExtendedConfig<?, ?, ?> eConfig : this.getConfigurables()) {
            // Re-save additional properties
            for(ConfigurablePropertyData configProperty : eConfig.configProperties.values()) {
                configProperty.saveToField();
                eConfig.onConfigPropertyReload(configProperty, reload);
            }
        }
    }

    public static ModConfig.Type modConfigLocationToType(ModConfigLocation modConfigLocation) {
        switch (modConfigLocation) {
            case COMMON -> {
                return ModConfig.Type.COMMON;
            }
            case CLIENT -> {
                return ModConfig.Type.CLIENT;
            }
            case SERVER -> {
                return ModConfig.Type.SERVER;
            }
            case STARTUP -> {
                throw new UnsupportedOperationException("Startup is not supported yet.");
            }
        }
        return null;
    }

    @Override
    public void addToConfigDictionary(ExtendedConfig<?, ?, ?> e) {
        super.addToConfigDictionary(e);
        if (e instanceof FluidConfigForge<?>) {
            getConfigDictionary().put(e.getNamedId(), e);
        }
    }
}
