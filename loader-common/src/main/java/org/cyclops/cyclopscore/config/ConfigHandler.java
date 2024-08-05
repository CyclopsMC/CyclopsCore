package org.cyclops.cyclopscore.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeAction;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigForge;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Create config file and register items and blocks from the given ExtendedConfigs.
 * @author rubensworks
 *
 */
public abstract class ConfigHandler {

    private final IModBase mod;
    private final LinkedHashSet<ExtendedConfig<?, ?, ?>> configurables = new LinkedHashSet<>();
    private final Map<String, ExtendedConfig<?, ?, ?>> configDictionary = Maps.newHashMap();
    private final Set<String> categories = Sets.newHashSet();
    private final Map<String, ConfigurablePropertyData> commandableProperties = Maps.newHashMap();

    public ConfigHandler(IModBase mod) {
        this.mod = mod;
    }

    public IModBase getMod() {
        return mod;
    }

    public LinkedHashSet<ExtendedConfig<?, ?, ?>> getConfigurables() {
        return configurables;
    }

    public Map<String, ExtendedConfig<?, ?, ?>> getConfigDictionary() {
        return configDictionary;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public Map<String, ConfigurablePropertyData> getCommandableProperties() {
        return commandableProperties;
    }

    public boolean addConfigurable(ExtendedConfig<?, ?, ?> e) {
        addToConfigDictionary(e);
        return configurables.add(e);
    }

    public void addToConfigDictionary(ExtendedConfig<?, ?, ?> e) {
        if (e instanceof BlockConfigCommon || e instanceof ItemConfigCommon) {
            configDictionary.put(e.getNamedId(), e);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs to read/write the config and register the given elements
     * This also sets the config of this instance.
     * This is called during mod construction.
     */
    public void loadModInit() {
        for (ExtendedConfig<?, ?, ?> eConfig : this.configurables) {
            mod.log(Level.TRACE, "Registering " + eConfig.getNamedId());
            eConfig.getConfigurableType().getConfigurableTypeAction().onRegisterModInit(eConfig);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs and call {@link ConfigurableTypeAction#onRegisterForge(ExtendedConfig)}
     * during the net.neoforged.neoforge.registries.NewRegistryEvent event.
     */
    public void loadForgeRegistries() {
        for (ExtendedConfig<?, ?, ?> eConfig : this.configurables) {
            eConfig.getConfigurableType().getConfigurableTypeAction().onRegisterForge(eConfig);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs and call {@link ConfigurableTypeAction#onRegisterForgeFilled(ExtendedConfig)}
     * during the RegisterEvent event.
     */
    public void loadForgeRegistriesFilled() {
        for (ExtendedConfig<?, ?, ?> eConfig : this.configurables) {
            eConfig.getConfigurableType().getConfigurableTypeAction().onRegisterForgeFilled(eConfig);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs and call {@link ConfigurableTypeAction#onRegisterSetup(ExtendedConfig)}
     * during net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent.
     */
    public void loadSetup() {
        for (ExtendedConfig<?, ?, ?> eConfig : this.configurables) {
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
     * Get the map of config nameid to config.
     * @return The dictionary.
     */
    public Map<String, ExtendedConfig<?, ?, ?>> getDictionary() {
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
    public abstract <V> void registerToRegistry(Registry<? super V> registry,
                                       ExtendedConfigForge<?, V, ?> config,
                                       @Nullable Callable<?> callback);

    public static ResourceLocation getConfigId(ExtendedConfigForge<?, ?, ?> config) {
        return ResourceLocation.fromNamespaceAndPath(config.getMod().getModId(), config.getNamedId());
    }

    /**
     * @param annotation The annotation to define the prefix for.
     * @return The prefix that will be used inside the config file for {@link ConfigurablePropertyCommon}'s.
     */
    protected String getConfigPropertyPrefix(ExtendedConfig<?, ?, ?> config, ConfigurablePropertyCommon annotation) {
        return annotation.namedId().isEmpty() ? config.getNamedId() : annotation.namedId();
    }

    public void generateConfigProperties(ExtendedConfig<?, ?, ?> config) throws IllegalArgumentException, IllegalAccessException {
        for(Field field : this.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(ConfigurablePropertyCommon.class)) {
                ConfigurablePropertyCommon annotation = field.getAnnotation(ConfigurablePropertyCommon.class);
                ConfigurablePropertyData<?> configProperty = new ConfigurablePropertyData<>(
                        getMod(),
                        annotation.category(),
                        getConfigPropertyPrefix(config, annotation) + "." + field.getName(),
                        field.get(null),
                        annotation.comment(),
                        annotation.isCommandable(),
                        annotation.configLocation(),
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
}
