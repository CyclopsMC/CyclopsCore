package org.cyclops.cyclopscore.config.extendedconfig;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurablePropertyData;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.CyclopsCoreConfigException;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

/**
 * A config that refers to a configurable.
 * Every unique configurable must have one unique extension of this class.
 *
 * This extension can contain several configurable settings and properties
 * that can also be set in the config file.
 *
 * @author rubensworks
 * @param <C> Class of the extension of ExtendedConfig
 * @param <I> The instance corresponding to this config.
 */
public abstract class ExtendedConfig<C extends ExtendedConfig<C, I>, I>
        implements Comparable<ExtendedConfig<C, I>> {

    @Getter
    private final ModBase mod;
    private final boolean enabledDefault;
    private ForgeConfigSpec.BooleanValue propertyEnabled;
    @Getter
    private final String namedId;
    @Getter
    @Nullable
    private final String comment;
    @Getter
    private final Function<C, ? extends I> elementConstructor;

    private I instance;
    
    /**
     * A list of {@link ConfigurablePropertyData} that can contain additional settings for this configurable.
     */
    public List<ConfigurablePropertyData<?>> configProperties = Lists.newLinkedList();

    /**
     * Create a new config
     * @param mod The mod instance.
     * @param enabledDefault If this should is enabled by default. If this is false, this can still
     *                       be enabled through the config file.
     * @param namedId A unique name id
     * @param comment A comment that can be added to the config file line
     * @param elementConstructor The element constructor.
     */
    public ExtendedConfig(ModBase mod, boolean enabledDefault, String namedId, @Nullable String comment,
                          Function<C, ? extends I> elementConstructor) {
        this.mod = mod;
    	this.enabledDefault = enabledDefault;
    	this.namedId = namedId.toLowerCase(Locale.ROOT);
    	this.comment = comment;
    	this.elementConstructor = elementConstructor;
        try {
            generateConfigProperties();
        } catch (IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

	/**
     * Generate the list of ConfigProperties by checking all the fields with the ConfigurableProperty
     * annotation.
     * 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void generateConfigProperties() throws IllegalArgumentException, IllegalAccessException {
        for(Field field : this.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(ConfigurableProperty.class)) {
            	ConfigurableProperty annotation = field.getAnnotation(ConfigurableProperty.class);
                ConfigurablePropertyData<?> configProperty = new ConfigurablePropertyData<>(
                        getMod(),
                        annotation.category(),
                        getConfigPropertyPrefix() + "." + field.getName(),
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
                configProperties.add(configProperty);
            }
        }
    }
    
    /**
     * @return The prefix that will be used inside the config file for {@link ConfigurableProperty}'s.
     */
    protected String getConfigPropertyPrefix() {
		return this.getNamedId();
	}

	/**
     * Save this config inside the correct element and inside the implementation if itself.
     */
    public void save() {
        try {
            // Construct the instance
            if (this.getConfigurableType().hasUniqueInstance()) {
                I instance = this.getElementConstructor().apply(this.downCast());
                if(this.instance == null) {
                    this.instance = instance;
                } else {
                    showDoubleInitError();
                }
            }
        } catch (RuntimeException e) {
            mod.getLoggerHelper().getLogger().error(String.format("Registering %s caused an issue. ", getNamedId()), e);
            throw new CyclopsCoreConfigException(String.format("Registering %s caused the issue: %s",
                    this.getNamedId(), e.getMessage()));
        }
    }
    
    /**
     * Return the configurable type for which this config holds data
     * @return the type of the configurable to where the config belongs
     */
    public abstract ConfigurableType getConfigurableType();
    
    /**
     * Get the unlocalized name (must be unique!) for this configurable.
     * @return The unlocalized name.
     */
    public abstract String getTranslationKey();

    /**
     * Get the full unlocalized name for this configurable.
     * @return The unlocalized name.
     */
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    /**
     * @return Instance of the object this config refers to.
     */
    public I getInstance() {
        return this.instance;
    }
    
    /**
     * Will return the unique name of the object this config refers to
     * @return unique name of sub object
     */
    public String getSubUniqueName() {
        return getNamedId();
    }
    
    /**
     * Overridable method that is immediately called after the element of this config is registered.
     */
    public void onRegistered() {

    }

    /**
     * Overridable method that is immediately called after this element has been registered into a Forge registry.
     */
    public void onForgeRegistered() {

    }
    
    @Override
    public int compareTo(ExtendedConfig<C, I> o) {
        return getNamedId().compareTo(o.getNamedId());
    }

    /**
     * @return If the target should be enabled by default.
     */
    public boolean isEnabledDefault() {
        return enabledDefault;
    }

    /**
     * Checks if the eConfig refers to a target that should be enabled.
     * @return if the target should be enabled.
     */
    public boolean isEnabled() {
        Objects.requireNonNull(this.propertyEnabled, "No enabled property was found for " + this.getNamedId()
                + ", probably because it can not be disabled.");
        return this.propertyEnabled.get() && !isHardDisabled();
    }
    
    /**
     * Set the enabling of the target.
     * @param enabled If the target should be enabled.
     */
    public void setEnabled(boolean enabled) {
		this.propertyEnabled.set(enabled);
        this.propertyEnabled.save();
	}

    /**
     * Save the property for checking if the target is enabled.
     * @param propertyEnabled A boolean property.
     */
    public void setPropertyEnabled(ForgeConfigSpec.BooleanValue propertyEnabled) {
        this.propertyEnabled = propertyEnabled;
    }

    /**
     * If the target should be hard-disabled, this means no occurence in the config file,
     * total ignorance.
     * @return if the target should run trough the config handler.
     */
    public boolean isHardDisabled() {
        return false;
    }
    
    /**
     * Override this method to prevent configs to be disabled from the config file. (non-zero id's that is)
     * @return if the target can be disabled.
     */
    public boolean isDisableable() {
        return true;
    }
    
    /**
     * Call this method in the initInstance method of Configurables if the instance was already set.
     */
    public void showDoubleInitError() {
        String message = this.getClass()+" caused a double registration of " + getInstance() + ". This is an error in the mod code.";
        mod.log(Level.FATAL, message);
        throw new CyclopsCoreConfigException(message);
    }
    
    /**
     * Get the lowest castable config.
     * @return The downcasted config.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public C downCast() {
        return (C) this;
    }

}
