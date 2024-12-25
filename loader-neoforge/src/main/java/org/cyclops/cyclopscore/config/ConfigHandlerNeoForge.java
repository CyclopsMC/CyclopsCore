package org.cyclops.cyclopscore.config;

import com.google.common.base.Supplier;
import com.google.common.collect.*;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfigNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author rubensworks
 */
public class ConfigHandlerNeoForge extends ConfigHandlerCommon {

    private final Multimap<String, Pair<ExtendedConfigRegistry<?, ?, ?>, Callable<?>>> registryEntriesHolder = Multimaps.newListMultimap(Maps.<String, Collection<Pair<ExtendedConfigRegistry<?, ?, ?>, Callable<?>>>>newHashMap(), new Supplier<List<Pair<ExtendedConfigRegistry<?, ?, ?>, Callable<?>>>>() {
        // Compiler complains when this is replaced with a lambda :-(
        @Override
        public List<Pair<ExtendedConfigRegistry<?, ?, ?>, Callable<?>>> get() {
            return Lists.newArrayList();
        }
    });
    private Set<String> registryEventPassed = Sets.newHashSet();

    public ConfigHandlerNeoForge(ModBaseNeoForge<?> mod) {
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
        this.registryEventPassed.add(event.getRegistryKey().toString());
        Registry<?> registry = event.getRegistry();

        registryEntriesHolder.get(registry.key().toString()).forEach((pair) -> {
            ExtendedConfigRegistry<?, ?, ?> config = pair.getLeft();
            event.register(registry.key(), getConfigId(config), (Supplier) config::getInstance);
            try {
                if (pair.getRight() != null) {
                    pair.getRight().call();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Initialize the configs by running builders through all relevant parts.
     * @param configInitializers A collection of additional initializers to run the config builders through.
     */
    public void initialize(Collection<IConfigInitializer> configInitializers) {
        Map<ModConfig.Type, ModConfigSpec.Builder> configBuilders = new EnumMap<>(ModConfig.Type.class);

        // Pass config builder to all configurables
        for (ExtendedConfigCommon<?, ?, ?> eConfig : getConfigurables()) {
            ModConfigSpec.Builder configBuilder = configBuilders.get(ModConfig.Type.COMMON);
            if (configBuilder == null) {
                configBuilder = new ModConfigSpec.Builder();
                configBuilders.put(ModConfig.Type.COMMON, configBuilder);
            }
            addCategory(eConfig.getConfigurableType().getCategory());

            // Save additional properties
            for (ConfigurablePropertyData configProperty : eConfig.configProperties.values()) {
                ModConfigSpec.Builder configBuilderProperty = configBuilders.get(modConfigLocationToType(configProperty.getConfigLocation()));
                if (configBuilderProperty == null) {
                    configBuilderProperty = new ModConfigSpec.Builder();
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
        for (Map.Entry<ModConfig.Type, ModConfigSpec.Builder> entry : configBuilders.entrySet()) {
            ModLoadingContext.get().getActiveContainer().registerConfig(entry.getKey(), entry.getValue().build());
        }
    }

    protected <T> void onConfigPropertyInit(ConfigurablePropertyData<T> configPropertyData, ModConfigSpec.Builder configBuilder) {
        configBuilder.push(configPropertyData.category);

        // Construct property for enabling the configurable
        if (configPropertyData.requiresWorldRestart) {
            configBuilder.worldRestart();
        }
        ModConfigSpec.ConfigValue<T> configProperty = configBuilder
                .comment(configPropertyData.comment)
                .translation(configPropertyData.getLanguageKey())
                .define(configPropertyData.name, configPropertyData.defaultValue);
        configPropertyData.setConfigProperty(configProperty, (newValue) -> {
            configProperty.set(newValue);
            configProperty.save();
        });

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
        for(ExtendedConfigCommon<?, ?, ?> eConfig : this.getConfigurables()) {
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
                return ModConfig.Type.STARTUP;
            }
        }
        return null;
    }

    @Override
    public void addToConfigDictionary(ExtendedConfigCommon<?, ?, ?> e) {
        super.addToConfigDictionary(e);
        if (e instanceof FluidConfigNeoForge) {
            getConfigDictionary().put(e.getNamedId(), e);
        }
    }
}
