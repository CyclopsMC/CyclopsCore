package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A holder class for properties that go inside the config file.
 * Used inside the {@link ConfigHandlerCommon} for configuring the settings of the configurable.
 * Do no confuse with {@link ConfigurablePropertyCommon} which is an annotation an is internally used to
 * make new instances of {@link ConfigurablePropertyData}.
 * @author rubensworks
 * @param <T> The property value.
 */
public final class ConfigurablePropertyData<T> {

    public final IModBase mod;
    public final String category;
    public final String name;
    public final T defaultValue;
    public final String comment;
    public final boolean isCommandable;
    public final ModConfigLocation configLocation;
    public final Field field;
    public final boolean requiresWorldRestart;
    public final boolean requiresMcRestart;
    public final boolean showInGui;
    public final int minValue;
    public final int maxValue;

    private Supplier<T> configProperty;
    private Consumer<T> configPropertyUpdater;

    /**
     * Define a new configurable property.
     * @param mod The owner mod.
     * @param category Category.
     * @param name Name of the property.
     * @param defaultValue Value of the property.
     * @param comment Comment of the property for in the config file.
     * @param isCommandable If this property should be able to be changed at runtime via commands.
     * @param configLocation The config location.
     * @param field The field of the {@link ExtendedConfigCommon} this property refers to.
     * @param requiresWorldRestart If this property requires a world restart after change.
     * @param requiresMcRestart If this property requires a minecraft restart after change.
     * @param showInGui If this property should be editable via the gui.
     * @param minValue The minimal int value.
     * @param maxValue The maximal int value.
     */
    public ConfigurablePropertyData(IModBase mod, String category, String name, T defaultValue, String comment,
                                    boolean isCommandable, ModConfigLocation configLocation,
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

    public void setConfigProperty(Supplier<T> configProperty, Consumer<T> configPropertyUpdater) {
        this.configProperty = configProperty;
        this.configPropertyUpdater = configPropertyUpdater;
    }

    public Supplier<T> getConfigProperty() {
        return configProperty;
    }

    public Consumer<T> getConfigPropertyUpdater() {
        return configPropertyUpdater;
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

    public ModConfigLocation getConfigLocation() {
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
        } catch (IllegalStateException e) {
            // Ignore illegal state exceptions thrown by Forge if config is being read too early
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
