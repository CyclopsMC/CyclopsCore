package org.cyclops.cyclopscore.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.init.ModBase;

import java.lang.reflect.Field;

/**
 * A holder class for properties that go inside the config file.
 * Used inside the {@link ConfigHandler} for configuring the settings of the configurable.
 * Do no confuse with {@link ConfigurableProperty} which is an annotation an is internally used to
 * make new instances of {@link ConfigurablePropertyData}.
 * @author rubensworks
 * @param <T> The property value.
 */
public final class ConfigurablePropertyData<T> {

    private final ModBase mod;
    private final String category;
    private final String name;
    private final T defaultValue;
    private final String comment;
    private final boolean isCommandable;
    private final ModConfig.Type configLocation;
    private final Field field;
    private final boolean requiresWorldRestart;
    private final boolean requiresMcRestart;
    private final boolean showInGui;
    private final int minValue;
    private final int maxValue;

    private ForgeConfigSpec.ConfigValue<T> configProperty;

    /**
     * Define a new configurable property.
     * @param mod The owner mod.
     * @param category Category.
     * @param name Name of the property.
     * @param defaultValue Value of the property.
     * @param comment Comment of the property for in the config file.
     * @param isCommandable If this property should be able to be changed at runtime via commands.
     * @param configLocation The config location.
     * @param field The field of the {@link ExtendedConfig} this property refers to.
     * @param requiresWorldRestart If this property requires a world restart after change.
     * @param requiresMcRestart If this property requires a minecraft restart after change.
     * @param showInGui If this property should be editable via the gui.
     * @param minValue The minimal int value.
     * @param maxValue The maximal int value.
     */
    public ConfigurablePropertyData(ModBase mod, String category, String name, T defaultValue, String comment,
                                    boolean isCommandable, ModConfig.Type configLocation,
                                    Field field, boolean requiresWorldRestart, boolean requiresMcRestart, boolean showInGui, int minValue, int maxValue) {
        this.mod = mod;
        this.category = category;
        this.name = name;
        this.defaultValue = defaultValue;
        this.comment = comment;
        this.isCommandable = isCommandable;
        this.field = field;
        this.configLocation = configLocation;
        this.requiresWorldRestart = requiresWorldRestart;
        this.requiresMcRestart = requiresMcRestart;
        this.showInGui = showInGui;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public void onConfigInit(ForgeConfigSpec.Builder configBuilder) {
        configBuilder.push(this.category);

        // Construct property for enabling the configurable
        if (this.requiresWorldRestart) {
            configBuilder.worldRestart();
        }
        // TODO: generalize .define with specific subtypes (like number ranges and predicates)
        ForgeConfigSpec.ConfigValue<T> configProperty = configBuilder
                .comment(this.comment)
                .translation(getLanguageKey())
                .define(this.name, this.defaultValue);
        setConfigProperty(configProperty);

        configBuilder.pop();
    }

    private void setConfigProperty(ForgeConfigSpec.ConfigValue<T> configProperty) {
        this.configProperty = configProperty;
    }

    public ForgeConfigSpec.ConfigValue<T> getConfigProperty() {
        return configProperty;
    }

    /**
     * @return Can this property be changed through commands.
     */
    public boolean isCommandable() {
        return isCommandable;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public ModConfig.Type getConfigLocation() {
        return configLocation;
    }

    /**
	 * @return the LanguageKey
	 */
	public String getLanguageKey() {
		return "config." + mod.getModId() + "." + name.replaceAll("\\s", "");
	}

    public void saveToField() {
        try {
            field.set(null, this.configProperty.get());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
