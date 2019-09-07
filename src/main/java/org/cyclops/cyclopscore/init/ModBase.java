package org.cyclops.cyclopscore.init;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import lombok.Data;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.client.icon.IconProvider;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.client.key.KeyRegistry;
import org.cyclops.cyclopscore.command.CommandConfig;
import org.cyclops.cyclopscore.command.CommandVersion;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.helper.LoggerHelper;
import org.cyclops.cyclopscore.modcompat.IMCHandler;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.modcompat.capabilities.CapabilityConstructorRegistry;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.persist.world.WorldStorage;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Base class for mods which adds a few convenience methods.
 * Dont forget to call the supers for the init events.
 * @author rubensworks
 */
@Data
public abstract class ModBase<T extends ModBase> {

    public static final EnumReferenceKey<String> REFKEY_TEXTURE_PATH_GUI = EnumReferenceKey.create("texture_path_gui", String.class);
    public static final EnumReferenceKey<String> REFKEY_TEXTURE_PATH_MODELS = EnumReferenceKey.create("texture_path_models", String.class);
    public static final EnumReferenceKey<String> REFKEY_TEXTURE_PATH_SKINS = EnumReferenceKey.create("texture_path_skins", String.class);
    public static final EnumReferenceKey<String> REFKEY_MOD_VERSION = EnumReferenceKey.create("mod_version", String.class);
    public static final EnumReferenceKey<Boolean> REFKEY_RETROGEN = EnumReferenceKey.create("retrogen", Boolean.class);
    public static final EnumReferenceKey<Boolean> REFKEY_CRASH_ON_INVALID_RECIPE = EnumReferenceKey.create("crash_on_invalid_recipe", Boolean.class);
    public static final EnumReferenceKey<Boolean> REFKEY_CRASH_ON_MODCOMPAT_CRASH = EnumReferenceKey.create("crash_on_modcompat_crash", Boolean.class);
    public static final EnumReferenceKey<Boolean> REFKEY_INFOBOOK_REWARDS = EnumReferenceKey.create("rewards", Boolean.class);

    private final String modId, modName;
    private final LoggerHelper loggerHelper;
    private final ConfigHandler configHandler;
    private final Map<EnumReferenceKey<?>, Object> genericReference = Maps.newHashMap();
    private final List<WorldStorage> worldStorages = Lists.newLinkedList();
    private final RegistryManager registryManager;
    private final RecipeHandler recipeHandler;
    private final IKeyRegistry keyRegistry;
    private final PacketHandler packetHandler;
    private final ModCompatLoader modCompatLoader;
    private final CapabilityConstructorRegistry capabilityConstructorRegistry;
    private final IMCHandler imcHandler;

    private ICommonProxy proxy;

    private ItemGroup defaultCreativeTab = null;

    public ModBase(String modId, String modName, Consumer<T> instanceSetter) {
        instanceSetter.accept((T) this);
        this.modId = modId;
        this.modName = modName;
        this.loggerHelper = constructLoggerHelper();
        this.configHandler = constructConfigHandler();
        this.registryManager = constructRegistryManager();
        this.recipeHandler = constructRecipeHandler();
        this.keyRegistry = new KeyRegistry();
        this.packetHandler = constructPacketHandler();
        this.modCompatLoader = constructModCompatLoader();
        this.capabilityConstructorRegistry = constructCapabilityConstructorRegistry();
        this.imcHandler = constructIMCHandler();

        // Register listeners
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOWEST, this::afterRegistriesCreated);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.HIGHEST, this::beforeRegistriedFilled);

        // Register proxies
        DistExecutor.runForDist(
                () -> () -> this.proxy = this.constructClientProxy(),
                () -> () -> this.proxy = this.constructCommonProxy()
        );

        populateDefaultGenericReferences();

        // Initialize config handler
        this.onConfigsRegister(getConfigHandler());
        getConfigHandler().initialize(Lists.newArrayList(
                getModCompatLoader()
        ));
        getConfigHandler().loadModInit();

        loadModCompats(getModCompatLoader());
    }

    protected LoggerHelper constructLoggerHelper() {
        return new LoggerHelper(this.modName);
    }

    protected ConfigHandler constructConfigHandler() {
        return new ConfigHandler(this);
    }

    protected RegistryManager constructRegistryManager() {
        return new RegistryManager();
    }

    @Deprecated // TODO: rm
    protected RecipeHandler constructRecipeHandler() {
        return null;
    }

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

    protected LiteralArgumentBuilder<CommandSource> constructBaseCommand() {
        LiteralArgumentBuilder<CommandSource> root = Commands.literal(this.getModId());

        root.then(CommandConfig.make(this));
        root.then(CommandVersion.make(this));

        return root;
    }

    /**
     * @return The icon provider that was constructed in {@link ClientProxyComponent}.
     */
    @OnlyIn(Dist.CLIENT)
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
        putGenericReference(REFKEY_CRASH_ON_INVALID_RECIPE, false);
        putGenericReference(REFKEY_CRASH_ON_MODCOMPAT_CRASH, false);
        putGenericReference(REFKEY_INFOBOOK_REWARDS, true);
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
     * Called on the Forge setup lifecycle event.
     * @param event The setup event.
     */
    protected void setup(FMLCommonSetupEvent event) {
        log(Level.TRACE, "setup()");

        // Iterate over all configs again
        getConfigHandler().loadSetup();

        // Register proxy things
        ICommonProxy proxy = getProxy();
        if(proxy != null) {
            proxy.registerEventHooks();
            proxy.registerRenderers();
            proxy.registerKeyBindings(getKeyRegistry());
            getPacketHandler().init();
            proxy.registerPacketHandlers(getPacketHandler());
            proxy.registerTickHandlers();
        }

        // Initialize the creative tab
        getDefaultItemGroup();
    }

    /**
     * Load things after Forge registries have been created.
     * @param event The Forge registry creation event.
     */
    private void afterRegistriesCreated(RegistryEvent.NewRegistry event) {
        getConfigHandler().loadForgeRegistries();
    }

    /**
     * Load things before Forge registries are being filled.
     * @param event The Forge registry filling event.
     */
    private void beforeRegistriedFilled(RegistryEvent.Register event) {
        // We only need to call this once, and the blocks event is emitted first.
        if (event.getRegistry() == ForgeRegistries.BLOCKS) {
            getConfigHandler().loadForgeRegistriesFilled();
        }
    }

    /**
     * Register the things that are related to when the server is starting.
     * @param event The Forge server starting event.
     */
    @SubscribeEvent
    protected void onServerStarting(FMLServerStartingEvent event) {
        event.getCommandDispatcher().register(constructBaseCommand());
    }

    /**
     * Register the things that are related to when the server is about to start.
     * @param event The Forge server about to start event.
     */
    @SubscribeEvent
    protected void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        for(WorldStorage worldStorage : worldStorages) {
            worldStorage.onAboutToStartEvent(event);
        }
    }

    /**
     * Register the things that are related to server starting.
     * @param event The Forge server started event.
     */
    @SubscribeEvent
    protected void onServerStarted(FMLServerStartedEvent event) {
        for(WorldStorage worldStorage : worldStorages) {
            worldStorage.onStartedEvent(event);
        }
    }

    /**
     * Register the things that are related to server stopping, like persistent storage.
     * @param event The Forge server stopping event.
     */
    @SubscribeEvent
    protected void onServerStopping(FMLServerStoppingEvent event) {
        for(WorldStorage worldStorage : worldStorages) {
            worldStorage.onStoppingEvent(event);
        }
    }

    /**
     * Register a new world storage type.
     * Make sure to call this at least before the event
     * {@link net.minecraftforge.fml.event.server.FMLServerStartedEvent} is called.
     * @param worldStorage The world storage to register.
     */
    public void registerWorldStorage(WorldStorage worldStorage) {
        worldStorages.add(worldStorage);
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract IClientProxy constructClientProxy();

    @OnlyIn(Dist.DEDICATED_SERVER)
    protected abstract ICommonProxy constructCommonProxy();

    /**
     * Construct an item group, will only be called once during the init event.
     * @return The default item group for items and blocks.
     */
    protected abstract ItemGroup constructDefaultItemGroup();

    /**
     * Called when the configs should be registered.
     * @param configHandler The config handler to register to.
     */
    protected void onConfigsRegister(ConfigHandler configHandler) {

    }

    /**
     * @return The default item group for items and blocks.
     */
    public final ItemGroup getDefaultItemGroup() {
        if(defaultCreativeTab == null) {
            defaultCreativeTab = constructDefaultItemGroup();
        }
        return defaultCreativeTab;
    }

    /**
     * @return The proxy for this mod.
     */
    public ICommonProxy getProxy() {
        return this.proxy;
    }

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
        ModContainer modContainer = ModList.get().getModContainerById(modId).orElse(null);
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
