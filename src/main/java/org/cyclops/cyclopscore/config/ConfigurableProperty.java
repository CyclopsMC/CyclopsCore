package org.cyclops.cyclopscore.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Property inside configs that can be configured in the config file.
 * @author rubensworks
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)
public @interface ConfigurableProperty {
    /**
     * The category of the field. categoryRaw can also be used to define direct strings as categories.
     * @return The category.
     */
	ConfigurableTypeCategory category();
	/**
     * The category of the field, as a raw string.
     * @return The category.
     */
	String categoryRaw() default "";
    /**
     * The comment for the field in the config file.
     * @return The comment.
     */
    String comment();
    /**
     * Whether or not this field can be changed with commands at runtime.
     * @return If it is commandable.
     */
    boolean isCommandable() default false;
    
    /**
     * @return If this configurable requires worlds to regenerate.
     */
    boolean requiresWorldRestart() default false;
    
    /**
     * @return If this configurable requires minecraft to restart.
     */
    boolean requiresMcRestart() default false;
    
    /**
     * @return If this configurable will be shown in the gui.
     */
    boolean showInGui() default true;
    
    /**
     * The minimal value of the field, as a integer.
     * @return The minimal value.
     */
    int minimalValue() default Integer.MIN_VALUE;
    
    /**
     * The maximal value of the field, as a integer.
     * @return The maximal value.
     */
    int maximalValue() default Integer.MAX_VALUE;
    
    /**
     * @return The optional callback when the property has been changed.
     * @see IChangedCallback
     */
    Class<? extends IChangedCallback> changedCallback() default IChangedCallback.class;
}
