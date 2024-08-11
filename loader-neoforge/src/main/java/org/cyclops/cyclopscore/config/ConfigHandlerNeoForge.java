package org.cyclops.cyclopscore.config;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigForge;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author rubensworks
 */
public class ConfigHandlerNeoForge extends ConfigHandler {

    private final Multimap<String, Pair<ExtendedConfigForge<?, ?, ?>, Callable<?>>> registryEntriesHolder = Multimaps.newListMultimap(Maps.<String, Collection<Pair<ExtendedConfigForge<?, ?, ?>, Callable<?>>>>newHashMap(), new Supplier<List<Pair<ExtendedConfigForge<?, ?, ?>, Callable<?>>>>() {
        // Compiler complains when this is replaced with a lambda :-(
        @Override
        public List<Pair<ExtendedConfigForge<?, ?, ?>, Callable<?>>> get() {
            return Lists.newArrayList();
        }
    });
    private Set<String> registryEventPassed = Sets.newHashSet();

    public ConfigHandlerNeoForge(ModBase<?> mod) {
        super(mod);
        mod.getModEventBus().register(this);
    }

    public Multimap<String, Pair<ExtendedConfigForge<?, ?, ?>, Callable<?>>> getRegistryEntriesHolder() {
        return registryEntriesHolder;
    }

    public Set<String> getRegistryEventPassed() {
        return registryEventPassed;
    }

    @Override
    public <V> void registerToRegistry(Registry<? super V> registry,
                                       ExtendedConfigForge<?, V, ?> config,
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
            ExtendedConfigForge<?, ?, ?> config = pair.getLeft();
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
        for (ExtendedConfig<?, ?, ?> eConfig : getConfigurables()) {
            ModConfigSpec.Builder configBuilder = configBuilders.get(ModConfig.Type.COMMON);
            if (configBuilder == null) {
                configBuilder = new ModConfigSpec.Builder();
                configBuilders.put(ModConfig.Type.COMMON, configBuilder);
            }
            addCategory(eConfig.getConfigurableType().getCategory());

            // Save additional properties
            for (ConfigurablePropertyData configProperty : eConfig.configProperties.values()) {
                ModConfigSpec.Builder configBuilderProperty = configBuilders.get(configProperty.getConfigLocation());
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

    protected String getConfigPropertyLegacyPrefix(ExtendedConfig<?, ?, ?> config, ConfigurableProperty annotation) { // TODO: rm in next major version
        return annotation.namedId().isEmpty() ? config.getNamedId() : annotation.namedId();
    }

    public static ModConfigLocation modConfigTypeToLocation(ModConfig.Type modConfigType) { // TODO: rm in next major version
        switch (modConfigType) {
            case COMMON -> {
                return ModConfigLocation.COMMON;
            }
            case CLIENT -> {
                return ModConfigLocation.CLIENT;
            }
            case SERVER -> {
                return ModConfigLocation.SERVER;
            }
            case STARTUP -> {
                return ModConfigLocation.STARTUP;
            }
        }
        return null;
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
    public void generateConfigProperties(ExtendedConfig<?, ?, ?> config) throws IllegalArgumentException, IllegalAccessException {
        super.generateConfigProperties(config);

        // TODO: rm in next major version
        for(Field field : this.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(ConfigurableProperty.class)) {
                ConfigurableProperty annotation = field.getAnnotation(ConfigurableProperty.class);
                ConfigurablePropertyData<?> configProperty = new ConfigurablePropertyData<>(
                        getMod(),
                        annotation.category(),
                        getConfigPropertyLegacyPrefix(config, annotation) + "." + field.getName(),
                        field.get(null),
                        annotation.comment(),
                        annotation.isCommandable(),
                        modConfigTypeToLocation(annotation.configLocation()),
                        field,
                        annotation.requiresWorldRestart(),
                        annotation.requiresMcRestart(),
                        annotation.showInGui(),
                        annotation.minimalValue(),
                        annotation.maximalValue());
                config.configProperties.put(configProperty.getName(), configProperty);
            }
        }
    }

    @Override
    public void addToConfigDictionary(ExtendedConfig<?, ?, ?> e) {
        super.addToConfigDictionary(e);
        if (e instanceof FluidConfig) {
            getConfigDictionary().put(e.getNamedId(), e);
        }
    }
}