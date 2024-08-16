package org.cyclops.cyclopscore.config.extendedconfig;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ConfigurablePropertyData;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.CyclopsCoreConfigException;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.Locale;
import java.util.Map;
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
 * @param <M> The mod type
 */
public abstract class ExtendedConfigCommon<C extends ExtendedConfigCommon<C, I, M>, I, M extends IModBase>
        implements Comparable<ExtendedConfigCommon<C, I, M>> {

    private final M mod;
    private final String namedId;
    private final Function<C, ? extends I> elementConstructor;

    private I instance;

    /**
     * A list of {@link ConfigurablePropertyData} that can contain additional settings for this configurable.
     */
    public Map<String, ConfigurablePropertyData<?>> configProperties = Maps.newHashMap();

    /**
     * Create a new config
     * @param mod The mod instance.
     * @param namedId A unique name id
     * @param elementConstructor The element constructor.
     */
    public ExtendedConfigCommon(M mod, String namedId, Function<C, ? extends I> elementConstructor) {
        this.mod = mod;
        this.namedId = namedId.toLowerCase(Locale.ROOT);
        this.elementConstructor = elementConstructor;
        try {
            mod.getConfigHandler().generateConfigProperties(this);
        } catch (IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    public M getMod() {
        return mod;
    }

    public String getNamedId() {
        return namedId;
    }

    public Function<C, ? extends I> getElementConstructor() {
        return elementConstructor;
    }

    protected void initializeInstance() {
        I instance = this.getElementConstructor().apply(this.downCast());
        if (this.instance == null) {
            this.instance = instance;
        } else {
            showDoubleInitError();
        }
    }

    /**
     * Save this config inside the correct element and inside the implementation if itself.
     */
    protected void save() {
        // Construct the instance
        if (this.getConfigurableType().hasUniqueInstance()) {
            this.initializeInstance();
        }
    }

    /**
     * Return the configurable type for which this config holds data
     * @return the type of the configurable to where the config belongs
     */
    public abstract ConfigurableTypeCommon getConfigurableType();

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
        if (!this.getConfigurableType().hasUniqueInstance()) {
            throw new IllegalStateException("Tried calling getInstance() on a config of type that has no unique instances.");
        }
        if (this.instance == null) {
            this.save();
        }
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
     * Overridable method that is called after the element of this config is fully registered,
     * e.g. after net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent.
     */
    public void onRegistered() {

    }

    /**
     * Overridable method that is immediately called after this element has been registered into a Forge registry.
     */
    public void onForgeRegistered() {

    }

    @Override
    public int compareTo(ExtendedConfigCommon<C, I, M> o) {
        return getNamedId().compareTo(o.getNamedId());
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

    /**
     * Called when the config is loaded or reloaded
     * @param reload If we are reloading, otherwise this is an initial load.
     * @param configProperty The config property
     */
    public void onConfigPropertyReload(ConfigurablePropertyData<?> configProperty, boolean reload) {

    }

    /**
     * @param annotation The annotation to define the prefix for.
     * @return The prefix that will be used inside the config file for {@link ConfigurablePropertyCommon}'s.
     */
    public String getConfigPropertyPrefix(ConfigurablePropertyCommon annotation) {
        return annotation.namedId().isEmpty() ? this.getNamedId() : annotation.namedId();
    }
}
