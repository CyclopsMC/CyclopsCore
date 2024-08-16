package org.cyclops.cyclopscore.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeActionCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
public abstract class ConfigHandler {

    private final IModBase mod;
    private final LinkedHashSet<ExtendedConfigCommon<?, ?, ?>> configurables = new LinkedHashSet<>();
    private final Map<String, ExtendedConfigCommon<?, ?, ?>> configDictionary = Maps.newHashMap();
    private final Set<String> categories = Sets.newHashSet();
    private final Map<String, ConfigurablePropertyData> commandableProperties = Maps.newHashMap();

    public ConfigHandler(IModBase mod) {
        this.mod = mod;
    }

    public IModBase getMod() {
        return mod;
    }

    public LinkedHashSet<ExtendedConfigCommon<?, ?, ?>> getConfigurables() {
        return configurables;
    }

    public Map<String, ExtendedConfigCommon<?, ?, ?>> getConfigDictionary() {
        return configDictionary;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public Map<String, ConfigurablePropertyData> getCommandableProperties() {
        return commandableProperties;
    }

    public boolean addConfigurable(ExtendedConfigCommon<?, ?, ?> e) {
        addToConfigDictionary(e);
        return configurables.add(e);
    }

    public void addToConfigDictionary(ExtendedConfigCommon<?, ?, ?> e) {
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
        for (ExtendedConfigCommon<?, ?, ?> eConfig : this.configurables) {
            mod.log(Level.TRACE, "Registering " + eConfig.getNamedId());
            eConfig.getConfigurableType().getConfigurableTypeAction().onRegisterModInit(eConfig);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs and call {@link ConfigurableTypeActionCommon#onRegisterForge(ExtendedConfigCommon)}
     * during the net.neoforged.neoforge.registries.NewRegistryEvent event.
     */
    public void loadForgeRegistries() {
        for (ExtendedConfigCommon<?, ?, ?> eConfig : this.configurables) {
            eConfig.getConfigurableType().getConfigurableTypeAction().onRegisterForge(eConfig);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs and call {@link ConfigurableTypeActionCommon#onRegisterForgeFilled(ExtendedConfigCommon)}
     * during the RegisterEvent event.
     */
    public void loadForgeRegistriesFilled() {
        for (ExtendedConfigCommon<?, ?, ?> eConfig : this.configurables) {
            eConfig.getConfigurableType().getConfigurableTypeAction().onRegisterForgeFilled(eConfig);
        }
    }

    /**
     * Iterate over the given ExtendedConfigs and call {@link ConfigurableTypeActionCommon#onRegisterSetup(ExtendedConfigCommon)}
     * during net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent.
     */
    public void loadSetup() {
        for (ExtendedConfigCommon<?, ?, ?> eConfig : this.configurables) {
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
    public Map<String, ExtendedConfigCommon<?, ?, ?>> getDictionary() {
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
                                       ExtendedConfigRegistry<?, V, ?> config,
                                       @Nullable Callable<?> callback);

    public static ResourceLocation getConfigId(ExtendedConfigCommon<?, ?, ?> config) {
        return ResourceLocation.fromNamespaceAndPath(config.getMod().getModId(), config.getNamedId());
    }

    /**
     * @param annotation The annotation to define the prefix for.
     * @return The prefix that will be used inside the config file for {@link ConfigurablePropertyCommon}'s.
     */
    protected String getConfigPropertyPrefix(ExtendedConfigCommon<?, ?, ?> config, ConfigurablePropertyCommon annotation) {
        return annotation.namedId().isEmpty() ? config.getNamedId() : annotation.namedId();
    }

    public void generateConfigProperties(ExtendedConfigCommon<?, ?, ?> config) throws IllegalArgumentException, IllegalAccessException {
        for(Field field : getAllFields(config.getClass())) {
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

    protected static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
}
