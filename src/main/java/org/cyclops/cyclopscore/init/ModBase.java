package org.cyclops.cyclopscore.init;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.Debug;
import org.cyclops.cyclopscore.GeneralConfig;
import org.cyclops.cyclopscore.client.icon.IconProvider;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.LoggerHelper;
import org.cyclops.cyclopscore.persist.world.WorldStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for mods which adds a few convenience methods.
 * Dont forget to call the supers for the init events.
 * @author rubensworks
 */
@Data
public abstract class ModBase {

    public static final EnumReferenceKey REFKEY_TEXTURE_PATH_GUI = EnumReferenceKey.create("texture_path_gui");

    private final String modId, modName;
    private final LoggerHelper loggerHelper;
    private final Set<IInitListener> initListeners;
    private final ConfigHandler configHandler;
    private final Map<EnumReferenceKey, String> genericReference = Maps.newHashMap();
    private final IconProvider iconProvider;
    private List<WorldStorage> worldStorages = Lists.newLinkedList();

    private CreativeTabs defaultCreativeTab = null;

    public ModBase(String modId, String modName) {
        this.modId = modId;
        this.modName = modName;
        this.loggerHelper = new LoggerHelper(this.modName);
        this.initListeners = Sets.newHashSet();
        this.configHandler = new ConfigHandler(this);
        this.iconProvider = new IconProvider(this);
        populateDefaultGenericReferences();
    }

    private void populateDefaultGenericReferences() {
        genericReference.put(REFKEY_TEXTURE_PATH_GUI, "textures/gui/");
    }

    /**
     * Get the value for a generic reference key.
     * The default keys can be found in {@link org.cyclops.cyclopscore.init.ModBase}.
     * @param key The key of a value.
     * @return The value for the given key.
     */
    public String getReferenceValue(EnumReferenceKey key) {
        if(!genericReference.containsKey(key)) throw new IllegalArgumentException("Could not find " + key + " as generic reference item.");
        return genericReference.get(key);
    }

    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public void log(String message) {
        log(Level.INFO, message);
    }

    /**
     * Log a new message of the given level for this mod.
     * @param level The level in which the message must be shown.
     * @param message The message to show.
     */
    public void log(Level level, String message) {
        loggerHelper.log(level, message);
    }

    /**
     * Register a new init listener.
     * @param initListener The init listener.
     */
    public void addInitListeners(IInitListener initListener) {
        synchronized(initListeners) {
            initListeners.add(initListener);
        }
    }

    /**
     * Get the init-listeners on a thread-safe way;
     * @return A copy of the init listeners list.
     */
    private Set<IInitListener> getSafeInitListeners() {
        Set<IInitListener> clonedInitListeners;
        synchronized(initListeners) {
            clonedInitListeners = Sets.newHashSet(initListeners);
        }
        return clonedInitListeners;
    }

    /**
     * Call the init-listeners for the given step.
     * @param step The step of initialization.
     */
    protected void callInitStepListeners(IInitListener.Step step) {
        for(IInitListener initListener : getSafeInitListeners()) {
            initListener.onInit(step);
        }
    }

    /**
     * Override this, call super and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler}.
     * @param event The pre-init event.
     */
    public void preInit(FMLPreInitializationEvent event) {
        log(Level.INFO, "preInit()");

        // Register configs and start with loading the general configs
        onGeneralConfigsRegister(getConfigHandler());
        getConfigHandler().handle(event);
        onMainConfigsRegister(getConfigHandler());

        // Call init listeners
        callInitStepListeners(IInitListener.Step.PREINIT);

        // Run debugging tools
        if(GeneralConfig.debug) {
            Debug.checkPreConfigurables(getConfigHandler());
        }

        // Load the rest of the configs and run the ConfigHandler to make/read the config and fill in the game registry
        getConfigHandler().handle(event);

        // Run debugging tools
        if(GeneralConfig.debug) {
            Debug.checkPostConfigurables();
        }
    }

    /**
     * Override this, call super and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler}.
     * @param event The init event.
     */
    public void init(FMLInitializationEvent event) {
        log(Level.INFO, "init()");

        // Initialize the creative tab
        getDefaultCreativeTab();

        // Polish the enabled configs.
        getConfigHandler().polishConfigs();
    }

    /**
     * Override this, call super and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler}.
     * @param event The post-init event.
     */
    public void postInit(FMLPostInitializationEvent event) {
        log(Level.INFO, "postInit()");
    }

    /**
     * Override this, call super and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler}.
     * Register the things that are related to server starting.
     * @param event The Forge event required for this.
     */
    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        for(WorldStorage worldStorage : worldStorages) {
            worldStorage.onStartedEvent(event);
        }
    }

    /**
     * Override this, call super and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler}.
     * Register the things that are related to server stopping, like persistent storage.
     * @param event The Forge event required for this.
     */
    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        for(WorldStorage worldStorage : worldStorages) {
            worldStorage.onStoppingEvent(event);
        }
    }

    /**
     * Register a new world storage type.
     * Make sure to call this at least before the event
     * {@link net.minecraftforge.fml.common.event.FMLServerStartedEvent} is called.
     * @param worldStorage The world storage to register.
     */
    public void registerWorldStorage(WorldStorage worldStorage) {
        worldStorages.add(worldStorage);
    }

    /**
     * Construct a creative tab, will only be called once during the init event.
     * @return The default creative tab for items and blocks.
     */
    public abstract CreativeTabs constructDefaultCreativeTab();

    /**
     * Register a config file.
     * The registration order is always kept.
     * @param extendedConfig The config to register.
     */
    public final void registerConfig(ExtendedConfig<?> extendedConfig) {
        getConfigHandler().add(extendedConfig);
    }

    /**
     * Called when the general configs should be registered.
     * These are configs which should be available before other configs can be registered.
     * @param configHandler The config handler to register to.
     */
    public void onGeneralConfigsRegister(ConfigHandler configHandler) {

    }

    /**
     * Called when the main configs should be registered.
     * @param configHandler The config handler to register to.
     */
    public void onMainConfigsRegister(ConfigHandler configHandler) {

    }

    /**
     * @return The default creative tab for items and blocks.
     */
    public final CreativeTabs getDefaultCreativeTab() {
        if(defaultCreativeTab == null) {
            defaultCreativeTab = constructDefaultCreativeTab();
        }
        return defaultCreativeTab;
    }

    @Data(staticConstructor = "create")
    private static class EnumReferenceKey {

        private final String key;

    }

}
