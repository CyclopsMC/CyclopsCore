package org.cyclops.cyclopscore.config.extendedconfig;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.*;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.init.IInitListener;
import org.cyclops.cyclopscore.init.ModBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * A config that refers to a {@link IConfigurable}. Every unique {@link IConfigurable} must have one
 * unique extension of this class. This contains several configurable settings and properties
 * that can also be set in the config file.
 * @author rubensworks
 * @param <C> Class of the extension of ExtendedConfig
 *
 */
public abstract class ExtendedConfig<C extends ExtendedConfig<C>> implements
	Comparable<ExtendedConfig<C>>, IInitListener {

    @Getter private final ModBase mod;
    private boolean enabled;
    @Getter private final String namedId;
    @Getter private final String comment;
    @SuppressWarnings("rawtypes")
    @Getter private final Class element;

    private IConfigurable overriddenSubInstance;
    
    /**
     * A list of {@link ConfigProperty} that can contain additional settings for this configurable.
     */
    public List<ConfigProperty> configProperties = Lists.newLinkedList();
    
    /**
     * Create a new config
     * @param mod     The mod instance.
     * @param enabled If this should is enabled by default. If this is false, this can still
     * be enabled through the config file.
     * @param namedId a unique name id
     * @param comment a comment that can be added to the config file line
     * @param element the class for the element this config is for
     */
    public ExtendedConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<?> element) {
        this.mod = mod;
    	this.enabled = enabled;
    	this.namedId = namedId;
    	this.comment = comment;
    	this.element = element;
        try {
            generateConfigProperties();
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e2) {
        	e2.printStackTrace();
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
            	IChangedCallback changedCallback = null;
            	if(annotation.changedCallback() != IChangedCallback.class) {
            		try {
						changedCallback = annotation.changedCallback().newInstance();
					} catch (InstantiationException e) {
						e.printStackTrace();
					}
            	}
            	String category = annotation.categoryRaw().equals("") ? annotation.category().toString() : annotation.categoryRaw();
                ConfigProperty configProperty = new ConfigProperty(
                        getMod(),
                		category,
                        getConfigPropertyPrefix() + "." + field.getName(),
                        field.get(null),
                        annotation.comment(),
                        new ConfigPropertyCallback(changedCallback),
                        annotation.isCommandable(),
                        field);
                configProperty.setRequiresWorldRestart(annotation.requiresWorldRestart());
                configProperty.setRequiresMcRestart(annotation.requiresMcRestart());
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
    @SuppressWarnings("unchecked")
    public void save() {
        try {
            // Save inside the self-implementation
            try {
                this.getClass().getField("_instance").set(null, this);
            } catch (NoSuchFieldError e) {
                throw new CyclopsCoreConfigException(String.format("The config file for %s requires a static field " +
                        "_instance.", this.getNamedId()));
            }

            // Try initalizing the override sub instance.
            this.overriddenSubInstance = initSubInstance();

            // Save inside the unique instance this config refers to (only if such an instance exists!)
            if (getOverriddenSubInstance() == null && this.getHolderType().hasUniqueInstance()) {
                Constructor constructor = this.getElement().getDeclaredConstructor(ExtendedConfig.class);
                if(constructor == null) {
                    throw new CyclopsCoreConfigException(String.format("The class %s requires a constructor with " +
                            "ExtendedConfig as single parameter.", this.getElement()));
                }
                Object instance = constructor.newInstance(this);

                Field field = this.getElement().getDeclaredField("_instance");
                field.setAccessible(true);
                if(field.get(null) == null) {
                    field.set(null, instance);
                } else {
                    showDoubleInitError();
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException |
                 InstantiationException | NoSuchMethodException e) {
            mod.getLoggerHelper().getLogger().error(String.format("Registering %s caused an issue. ", getNamedId()), e);
            throw new CyclopsCoreConfigException(String.format("Registering %s caused the issue: %s",
                    this.getNamedId(), e.getCause().getMessage()));
        } catch (InvocationTargetException e) {
            mod.log(Level.ERROR, "Registering " + this.getNamedId() + " caused the issue "
                    + "(skipping registration): " + e.getCause().getMessage());
            mod.getLoggerHelper().getLogger().error("Registering %s caused an issue. ", e.getCause());

            // Disable this configurable.
            if (!this.isDisableable()) {
                throw new CyclopsCoreConfigException("Registering " + this.getNamedId()
                        + " caused the issue: " + e.getCause().getMessage()
                        + ". Since this is a required element of this mod, we can not continue, "
                        + "there might be ID conflicts with other mods.");
            }
            this.setEnabled(false);
        }
    }
    
    /**
     * Return the configurable type for which this config holds data
     * @return the type of the configurable to where the config belongs
     */
    public abstract ConfigurableType getHolderType();
    
    /**
     * Get the unlocalized name (must be unique!) for this configurable.
     * @return The unlocalized name.
     */
    public abstract String getUnlocalizedName();

    /**
     * Get the full unlocalized name for this configurable.
     * @return The unlocalized name.
     */
    public String getFullUnlocalizedName() {
        return getUnlocalizedName();
    }

    /**
     * This method will by default just return null.
     * If it returns something else, this config will assume that the object that is returned is the unique sub-instance
     * for the configurable.
     * This is only called once.
     * @return A sub-instance that will become a singleton.
     */
    protected IConfigurable initSubInstance() {
        return null;
    }

    private IConfigurable getOverriddenSubInstance() {
        return this.overriddenSubInstance;
    }

    /**
     * Will return the instance of the object this config refers to
     * @return instance of sub object
     */
    @SuppressWarnings("unchecked")
    public IConfigurable getSubInstance() {
        if(getOverriddenSubInstance() != null) {
            return getOverriddenSubInstance();
        }
        if(!this.getHolderType().hasUniqueInstance())
            throw new CyclopsCoreConfigException("There exists no unique instance for " + this);
        try {
            Method method = this.getElement().getMethod("getInstance");
            if(method == null) {
                throw new CyclopsCoreConfigException("There exists no static getInstance method for  " + this);
            }
            return (IConfigurable) method.invoke(null);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
        	// Only possible in development mode
        	e1.printStackTrace();
        }

        return null;
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
    
    @Override
    public void onInit(IInitListener.Step step) {
    	
    }
    
    @Override
    public int compareTo(ExtendedConfig<C> o) {
        return getNamedId().compareTo(o.getNamedId());
    }
    
    /**
     * Checks if the eConfig refers to a target that should be enabled.
     * @return if the target should be enabled.
     */
    public boolean isEnabled() {
        return this.enabled && !isHardDisabled();
    }
    
    /**
     * Set the enabling of the target.
     * @param enabled If the target should be enabled.
     */
    public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
        String message = this.getClass()+" caused a double registration of "+getSubInstance()+". This is an error in the mod code.";
        mod.log(Level.FATAL, message);
        throw new CyclopsCoreConfigException(message);
    }
    
    /**
     * Get the lowest castable config.
     * @return The downcasted config.
     */
    @SuppressWarnings("unchecked")
    public C downCast() {
        C c = (C) this;
        return c;
    }
}
