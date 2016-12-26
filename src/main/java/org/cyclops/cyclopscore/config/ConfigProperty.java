package org.cyclops.cyclopscore.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.init.ModBase;

import java.lang.reflect.Field;

/**
 * A holder class for properties that go inside the config file.
 * Used inside the {@link ConfigHandler} for configuring the settings of the {@link IConfigurable}.
 * Do no confuse with {@link ConfigurableProperty} which is an annotation an is internally used to
 * make new instances of {@link ConfigProperty}.
 * @author rubensworks
 *
 */
public final class ConfigProperty {

    private final ModBase mod;
    private String category;
    private String name;
    private Object value;
    private String comment;
    private ConfigPropertyCallback callback;
    private boolean isCommandable;
    private Field field;
    private boolean requiresWorldRestart;
    private boolean requiresMcRestart;
    private boolean showInGui;    
    private int minValue;
    private int maxValue;	
    
    /**
     * Define a new configurable property.
     * @param mod The owner mod.
     * @param category Category.
     * @param name Name of the property.
     * @param value Value of the property.
     * @param comment Comment of the property for in the config file.
     * @param callback Callback object for when this property is configured.
     * @param isCommandable If this property should be able to be changed at runtime via commands.
     * @param field The field of the {@link ExtendedConfig} this property refers to.
     */
    public ConfigProperty(ModBase mod, String category, String name, Object value, String comment, ConfigPropertyCallback callback, boolean isCommandable, Field field) {
        this.mod = mod;
        this.category = category;
        this.name = name;
        this.value = value;
        this.comment = comment;
        this.callback = callback;
        this.isCommandable = isCommandable;
        this.field = field;
        callback.setConfigProperty(this);
    }
    
    /**
     * Define a new configurable property without a comment.
     * @param mod The owner mod.
     * @param category Category.
     * @param name Name of the property.
     * @param value Value of the property.
     * @param callback Callback object for when this property is configured.
     * @param isCommandable If this property should be able to be changed at runtime via commands.
     * @param field The field of the {@link ExtendedConfig} this property refers to.
     */
    public ConfigProperty(ModBase mod, String category, String name, Object value, ConfigPropertyCallback callback, boolean isCommandable, Field field) {
        this(mod, category, name, value, null, callback, isCommandable, field);
    }

    /**
     * Get the category.
     * @return The category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category.
     * @param category The category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the name.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     * @param name The name to be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value.
     * @return The value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the value.
     * @param value The value to be set.
     */
    public void setValue(Object value) {
        this.value = value;
        try {
            field.set(null, this.value);
        } catch (IllegalArgumentException | IllegalAccessException e1) {
            // Won't happen, trust me.
            e1.printStackTrace();
        }
    }

    /**
     * Get the comment.
     * @return The comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the comment.
     * @param comment The comment to be set.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Get the callback.
     * @return The callback.
     */
    public ConfigPropertyCallback getCallback() {
        return callback;
    }

    /**
     * Set the calback.
     * @param callback The callback to be set.
     */
    public void setCallback(ConfigPropertyCallback callback) {
        this.callback = callback;
    }
    
    /**
     * If this property can be configured with commands.
     * @return Is this commandable.
     */
    public boolean isCommandable() {
        return isCommandable;
    }

    /**
     * Set if this property can be configured with commands.
     * @param isCommandable True if this property can be configured with commands.
     */
    public void setCommandable(boolean isCommandable) {
        this.isCommandable = isCommandable;
    }
    
    /**
     * Save this property in the given config file.
     * @param config The config file to save to.
     */
    public void save(Configuration config) {
        save(config, false);
    }
    
    /**
     * Save this property in the given config file.
     * @param config The config file to save to.
     * @param forceUpdate If the value in the config file has to be overwritten.
     */
    public void save(Configuration config, boolean forceUpdate) {
        // Sorry, no cleaner solution for this...
        // Reflection could solve it partially, but it'd be still quite ugly...
        String category = getCategory();
        String name = getName();
        Object value = getValue();
        String comment = getComment();
        
        Property additionalProperty = null;
        if(value instanceof Integer) {
            additionalProperty = config.get(
                    category,
                    name,
                    (Integer)value,
                    comment
                    );
            if(forceUpdate) {
            	additionalProperty.setValue((Integer)value);
            }
            additionalProperty.setComment(getComment());
            if(forceUpdate) {
                getCallback().run((Integer)value);
            } else {
                getCallback().run(additionalProperty.getInt());
            }
        } else if(value instanceof Boolean) {
            additionalProperty = config.get(
                    category,
                    name,
                    (Boolean)value,
                    comment
                    );
            if(forceUpdate) {
            	additionalProperty.setValue((Boolean)value);
            }
            additionalProperty.setComment(getComment());
            if(forceUpdate) {
                getCallback().run((Boolean)value);
            } else {
                getCallback().run(additionalProperty.getBoolean((Boolean)value));
            }
            
        } else if(value instanceof Double) {
            additionalProperty = config.get(
                    category,
                    name,
                    (Double)value,
                    comment
                    );
            if(forceUpdate) {
            	additionalProperty.setValue((Double)value);
            }
            additionalProperty.setComment(getComment());
            if(forceUpdate) {
                getCallback().run((Double)value);
            } else {
                getCallback().run(additionalProperty.getDouble((Double)value));
            } 
        } else if(value instanceof String) {
            additionalProperty = config.get(
                    category,
                    name,
                    (String)value,
                    comment
                    );
            if(forceUpdate) {
            	additionalProperty.setValue((String)value);
            }
            additionalProperty.setComment(getComment());
            if(forceUpdate) {
                getCallback().run((String)value);
            } else {
                getCallback().run(additionalProperty.getString());
            }
        } else if(value instanceof String[]) {
            additionalProperty = config.get(
                    category,
                    name,
                    (String[])value,
                    comment
                    );
                if(forceUpdate) {
                	additionalProperty.setValues((String[])value);
                }
                additionalProperty.setComment(getComment());
                if(forceUpdate) {
                    getCallback().run((String[])value);
                } else {
                    getCallback().run(additionalProperty.getStringList());
                }
        } else if(value instanceof int[]) {
            additionalProperty = config.get(
                    category,
                    name,
                    (int[])value,
                    comment
            );
            if(forceUpdate) {
                additionalProperty.setValues((int[])value);
            }
            additionalProperty.setComment(getComment());
            if(forceUpdate) {
                getCallback().run((int[])value);
            } else {
                getCallback().run(additionalProperty.getIntList());
            }
        } else {
            mod.log(Level.ERROR, "Invalid config property class. No match found for '"
                    + name + "': '" + value + "'");
        }
        if(additionalProperty != null) {
            additionalProperty.setRequiresWorldRestart(isRequiresWorldRestart());
            additionalProperty.setRequiresMcRestart(isRequiresMcRestart());
        }

        if (value instanceof Integer || value instanceof Double) {
            additionalProperty.setMinValue(getMinValue());
            additionalProperty.setMaxValue(getMaxValue());
        }

        additionalProperty.setShowInGui(showInGui());
        additionalProperty.setLanguageKey(getLanguageKey());
    }

	/**
	 * @return the LanguageKey
	 */
	public String getLanguageKey() {
		return "config." + mod.getModId() + "." + getName().replaceAll("\\s", "");
	}    

	/**
	 * @return the requiresWorldRestart
	 */
	public boolean isRequiresWorldRestart() {
		return requiresWorldRestart;
	}

	/**
	 * @param requiresWorldRestart the requiresWorldRestart to set
	 */
	public void setRequiresWorldRestart(boolean requiresWorldRestart) {
		this.requiresWorldRestart = requiresWorldRestart;
	}

	/**
	 * @return the requiresMcRestart
	 */
	public boolean isRequiresMcRestart() {
		return requiresMcRestart;
	}

	/**
	 * @param requiresMcRestart the requiresMcRestart to set
	 */
	public void setRequiresMcRestart(boolean requiresMcRestart) {
		this.requiresMcRestart = requiresMcRestart;
	}

    /**
     * @param showInGui the showInGui to set
     */
    public void setShowInGui(boolean showInGui) {
        this.showInGui = showInGui;
    }

    /**
     * @return if item is shown in config GUI
     */
    public boolean showInGui() {
        return showInGui;
    }

    /**
     * @return the minValue
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * @param minValue minimal value of parameter
     */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the maxValue
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue maximal value of parameter
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}