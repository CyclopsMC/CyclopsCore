package org.cyclops.cyclopscore.init;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import net.minecraft.command.ICommand;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.client.gui.GuiHandler;
import org.cyclops.cyclopscore.client.icon.IconProvider;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.client.key.KeyRegistry;
import org.cyclops.cyclopscore.command.CommandMod;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.LoggerHelper;
import org.cyclops.cyclopscore.infobook.pageelement.AchievementRewardsAppendix;
import org.cyclops.cyclopscore.modcompat.IMCHandler;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.persist.world.WorldStorage;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

import java.io.File;
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

    public static final EnumReferenceKey<String> REFKEY_MOD_VERSION = EnumReferenceKey.create("mod_version", String.class);
    public static final EnumReferenceKey<String> REFKEY_TEXTURE_PATH_GUI = EnumReferenceKey.create("texture_path_gui", String.class);
    public static final EnumReferenceKey<String> REFKEY_TEXTURE_PATH_MODELS = EnumReferenceKey.create("texture_path_models", String.class);
    public static final EnumReferenceKey<String> REFKEY_TEXTURE_PATH_SKINS = EnumReferenceKey.create("texture_path_skins", String.class);
    public static final EnumReferenceKey<Boolean> REFKEY_RETROGEN = EnumReferenceKey.create("retrogen", Boolean.class);
    public static final EnumReferenceKey<Boolean> REFKEY_DEBUGCONFIG = EnumReferenceKey.create("debug_config", Boolean.class);
    public static final EnumReferenceKey<Boolean> REFKEY_CRASH_ON_INVALID_RECIPE = EnumReferenceKey.create("crash_on_invalid_recipe", Boolean.class);
    public static final EnumReferenceKey<Boolean> REFKEY_CRASH_ON_MODCOMPAT_CRASH = EnumReferenceKey.create("crash_on_modcompat_crash", Boolean.class);

    private final String modId, modName;
    private final LoggerHelper loggerHelper;
    private final Set<IInitListener> initListeners;
    private final ConfigHandler configHandler;
    private final Map<EnumReferenceKey, Object> genericReference = Maps.newHashMap();
    private final List<WorldStorage> worldStorages = Lists.newLinkedList();
    private final GuiHandler guiHandler;
    private final RegistryManager registryManager;
    private final RecipeHandler recipeHandler;
    private final IKeyRegistry keyRegistry;
    private final PacketHandler packetHandler;
    private final ModCompatLoader modCompatLoader;
    private final CapabilityConstructorRegistry capabilityConstructorRegistry;
    private final IMCHandler imcHandler;
    private final Debug debug;

    private CreativeTabs defaultCreativeTab = null;
    private File configFolder = null;

    public ModBase(String modId, String modName) {
        this.modId = modId;
        this.modName = modName;
        this.loggerHelper = constructLoggerHelper();
        this.initListeners = Sets.newHashSet();
        this.configHandler = constructConfigHandler();
        this.guiHandler = constructGuiHandler();
        this.registryManager = constructRegistryManager();
        this.recipeHandler = constructRecipeHandler();
        this.keyRegistry = new KeyRegistry();
        this.packetHandler = constructPacketHandler();
        this.modCompatLoader = constructModCompatLoader();
        this.capabilityConstructorRegistry = constructCapabilityConstructorRegistry();
        this.imcHandler = constructIMCHandler();
        this.debug = new Debug(this);

        populateDefaultGenericReferences();
        addInitListeners(getModCompatLoader());
        loadModCompats(getModCompatLoader());
    }

    protected LoggerHelper constructLoggerHelper() {
        return new LoggerHelper(this.modName);
    }

    protected ConfigHandler constructConfigHandler() {
        return new ConfigHandler(this);
    }

    protected GuiHandler constructGuiHandler() {
        return new GuiHandler(this);
    }

    protected RegistryManager constructRegistryManager() {
        return new RegistryManager();
    }

    protected abstract RecipeHandler constructRecipeHandler();

    protected PacketHandler constructPacketHandler() {
        return new PacketHandler(this);
    }

    protected ModCompatLoader constructModCompatLoader() {
        return new ModCompatLoader(this);
    }

    protected CapabilityConstructorRegistry constructCapabilityConstructorRegistry() {
        return new CapabilityConstructorRegistry(this);
    }

    protected IMCHandler constructIMCHandler() {
        return new IMCHandler(this);
    }

    protected ICommand constructBaseCommand() {
        return new CommandMod(this, Maps.<String, ICommand>newHashMap());
    }

    /**
     * @return The icon provider that was constructed in {@link ClientProxyComponent}.
     */
    @SideOnly(Side.CLIENT)
    public IconProvider getIconProvider() {
        return ((ClientProxyComponent) getProxy()).getIconProvider();
    }

    /**
     * Save a mod value.
     * @param key The key.
     * @param value The value.
     * @param <T> The value type.
     */
    public <T> void putGenericReference(EnumReferenceKey<T> key, T value) {
        genericReference.put(key, value);
    }

    private void populateDefaultGenericReferences() {
        putGenericReference(REFKEY_TEXTURE_PATH_GUI, "textures/gui/");
        putGenericReference(REFKEY_TEXTURE_PATH_MODELS, "textures/models/");
        putGenericReference(REFKEY_TEXTURE_PATH_SKINS, "textures/skins/");
        putGenericReference(REFKEY_RETROGEN, false);
        putGenericReference(REFKEY_DEBUGCONFIG, false);
        putGenericReference(REFKEY_CRASH_ON_INVALID_RECIPE, false);
        putGenericReference(REFKEY_CRASH_ON_MODCOMPAT_CRASH, false);
        putGenericReference(AchievementRewardsAppendix.REFKEY_REWARDS, true);
    }

    /**
     * This is called only once to let the mod compatibilities register themselves.
     * @param modCompatLoader The loader.
     */
    protected void loadModCompats(ModCompatLoader modCompatLoader) {

    }

    /**
     * Get the value for a generic reference key.
     * The default keys can be found in {@link org.cyclops.cyclopscore.init.ModBase}.
     * @param key The key of a value.
     * @param <T> The type of value.
     * @return The value for the given key.
     */
    @SuppressWarnings("unchecked")
    public <T> T getReferenceValue(EnumReferenceKey<T> key) {
        if(!genericReference.containsKey(key)) throw new IllegalArgumentException("Could not find " + key + " as generic reference item.");
        return (T) genericReference.get(key);
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
        log(Level.TRACE, "preInit()");

        if(getConfigFolder() == null) {
            // Determine config folder.
            String rootFolderName = event.getModConfigurationDirectory() + "/" + getModId();
            File configFolder = new File(rootFolderName);
            setConfigFolder(configFolder);
        }

        // Register configs and start with loading the general configs
        onGeneralConfigsRegister(getConfigHandler());
        getConfigHandler().handle(event);
        onMainConfigsRegister(getConfigHandler());

        // Call init listeners
        callInitStepListeners(IInitListener.Step.PREINIT);

        // Run debugging tools
        if(getReferenceValue(REFKEY_DEBUGCONFIG)) {
            getDebug().checkPreConfigurables(getConfigHandler());
        }

        // Load the rest of the configs and run the ConfigHandler to make/read the config and fill in the game registry
        getConfigHandler().handle(event);

        // Run debugging tools
        if(getReferenceValue(REFKEY_DEBUGCONFIG)) {
            getDebug().checkPostConfigurables();
        }

        // Register events
        ICommonProxy proxy = getProxy();
        if(proxy != null) {
            proxy.registerEventHooks();
        }
    }

    /**
     * Override this, call super and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler}.
     * @param event The init event.
     */
    public void init(FMLInitializationEvent event) {
        log(Level.TRACE, "init()");

        // Gui Handlers
        NetworkRegistry.INSTANCE.registerGuiHandler(getModId(), getGuiHandler());

        // Initialize the creative tab
        getDefaultCreativeTab();

        // Polish the enabled configs.
        getConfigHandler().polishConfigs();

        // Call init listeners
        callInitStepListeners(IInitListener.Step.INIT);

        // Register proxy related things.
        ICommonProxy proxy = getProxy();
        if(proxy != null) {
            proxy.registerRenderers();
            proxy.registerKeyBindings(getKeyRegistry());
            getPacketHandler().init();
            proxy.registerPacketHandlers(getPacketHandler());
            proxy.registerTickHandlers();
        }

        // Register recipes
        RecipeHandler recipeHandler = getRecipeHandler();
        if(recipeHandler != null) {
            recipeHandler.registerRecipes(getConfigFolder());
        }
    }

    /**
     * Override this, call super and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler}.
     * @param event The post-init event.
     */
    public void postInit(FMLPostInitializationEvent event) {
        log(Level.TRACE, "postInit()");

        // Call init listeners
        callInitStepListeners(IInitListener.Step.POSTINIT);
    }

    /**
     * Override this, call super and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler}.
     * Register the things that are related to when the server is starting.
     * @param event The Forge event required for this.
     */
    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(constructBaseCommand());
    }

    /**
     * Override this, call super and annotate with {@link net.minecraftforge.fml.common.Mod.EventHandler}.
     * Register the things that are related to when the server is about to start.
     * @param event The Forge event required for this.
     */
    @Mod.EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        for(WorldStorage worldStorage : worldStorages) {
            worldStorage.onAboutToStartEvent(event);
        }
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

    /**
     * @return The proxy for this mod, can be null if not required.
     */
    public abstract ICommonProxy getProxy();

    @Override
    public String toString() {
        return getModId();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return object == this;
    }

    /**
     * Get the mod by id.
     * @param modId The mod id.
     * @return The mod instance or null.
     */
    public static ModBase get(String modId) {
        ModContainer modContainer = Loader.instance().getIndexedModList().get(modId);
        Object mod = modContainer.getMod();
        if (mod instanceof ModBase) {
            return (ModBase) mod;
        }
        return null;
    }

    /**
     * Unique references to values that can be registered inside a mod.
     * @param <T> The type of value.
     */
    @Data
    public static class EnumReferenceKey<T> {

        private final String key;
        private final Class<T> type;

        private EnumReferenceKey(String key, Class<T> type) {
            this.key = key;
            this.type = type;
        }

        public static <T> EnumReferenceKey<T> create(String key, Class<T> type) {
            return new EnumReferenceKey<>(key, type);
        }

    }

}
