package org.cyclops.cyclopscore.modcompat.forgeconfig;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandlerFabric;
import org.cyclops.cyclopscore.config.ConfigurablePropertyData;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author rubensworks
 */
public class ConfigHandlerFabricHandler {

    private final ConfigHandlerFabric configHandler;

    public ConfigHandlerFabricHandler(ConfigHandlerFabric configHandler) {
        this.configHandler = configHandler;
    }

    public void onLoad(ModConfig configEvent) {
        this.configHandler.getMod().log(Level.TRACE, "Load config");
        syncProcessedConfigs(false);
    }

    public void onReload(ModConfig configEvent) {
        this.configHandler.getMod().log(Level.TRACE, "Reload config");
        syncProcessedConfigs(true);
    }

    /**
     * Initialize the configs by running builders through all relevant parts.
     * @param configInitializers A collection of additional initializers to run the config builders through.
     */
    public void initialize(Collection<IConfigInitializer> configInitializers) {
        NeoForgeModConfigEvents.loading(this.configHandler.getMod().getModId()).register(this::onLoad);
        NeoForgeModConfigEvents.reloading(this.configHandler.getMod().getModId()).register(this::onReload);

        Map<ModConfig.Type, ModConfigSpec.Builder> configBuilders = new EnumMap<>(ModConfig.Type.class);

        // Pass config builder to all configurables
        for (ExtendedConfig<?, ?, ?> eConfig : this.configHandler.getConfigurables()) {
            ModConfigSpec.Builder configBuilder = configBuilders.get(ModConfig.Type.COMMON);
            if (configBuilder == null) {
                configBuilder = new ModConfigSpec.Builder();
                configBuilders.put(ModConfig.Type.COMMON, configBuilder);
            }
            this.configHandler.addCategory(eConfig.getConfigurableType().getCategory());

            // Save additional properties
            for (ConfigurablePropertyData configProperty : eConfig.configProperties.values()) {
                ModConfigSpec.Builder configBuilderProperty = configBuilders.get(modConfigLocationToType(configProperty.getConfigLocation()));
                if (configBuilderProperty == null) {
                    configBuilderProperty = new ModConfigSpec.Builder();
                    configBuilders.put(modConfigLocationToType(configProperty.getConfigLocation()), configBuilderProperty);
                }
                this.configHandler.getCategories().add(configProperty.getCategory());
                this.onConfigPropertyInit(configProperty, configBuilder);
                if (configProperty.isCommandable()) {
                    this.configHandler.getCommandableProperties().put(configProperty.getName(), configProperty);
                }
            }
        }

        // Handle all config initializers
        for (IConfigInitializer configInitializer : configInitializers) {
            configInitializer.initializeConfig(configBuilders);
        }

        // Finalize config builders to config specs, and register them
        for (Map.Entry<ModConfig.Type, ModConfigSpec.Builder> entry : configBuilders.entrySet()) {
            NeoForgeConfigRegistry.INSTANCE.register(configHandler.getMod().getModId(), entry.getKey(), entry.getValue().build());
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
     * @param reload If we are reloading, otherwise this is an initial load.
     */
    @SuppressWarnings("unchecked")
    public void syncProcessedConfigs(boolean reload) {
        for(ExtendedConfig<?, ?, ?> eConfig : this.configHandler.getConfigurables()) {
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

}
