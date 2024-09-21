package org.cyclops.cyclopscore.init;

import com.google.common.collect.Lists;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.config.ConfigHandlerForge;
import org.cyclops.cyclopscore.helper.IModHelpersForge;
import org.cyclops.cyclopscore.helper.ModBaseCommon;
import org.cyclops.cyclopscore.helper.ModHelpersForge;
import org.cyclops.cyclopscore.neywork.PacketHandlerForge;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Base class for mods which adds a few convenience methods.
 * @author rubensworks
 */
public abstract class ModBaseForge<T extends ModBaseForge<T>> extends ModBaseCommon<T> {

    private final ICommonProxyCommon proxy;
    private final ConfigHandlerCommon configHandler;
    private final IEventBus modEventBus;
    private final PacketHandlerForge packetHandler;

    private boolean loaded = false;

    public ModBaseForge(String modId, Consumer<T> instanceSetter) {
        super(modId, instanceSetter);
        this.modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        this.proxy = getModHelpers().getMinecraftHelpers().isClientSide() ? this.constructClientProxy() : this.constructCommonProxy();
        this.configHandler = constructConfigHandler();
        this.packetHandler = constructPacketHandler();

        // Register listeners
        getModEventBus().addListener(this::setup);
        getModEventBus().addListener(EventPriority.LOWEST, this::afterRegistriesCreated);
        getModEventBus().addListener(EventPriority.HIGHEST, this::beforeRegistriedFilled);
        getModEventBus().addListener(this::loadComplete);
        if (getModHelpers().getMinecraftHelpers().isClientSide()) {
            getModEventBus().addListener(this::setupClient);
        }
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);

        // Initialize config handler
        this.onConfigsRegister(getConfigHandler());
        getConfigHandler().initialize(Lists.newArrayList());
        getConfigHandler().loadModInit();

        loadModCompats(getModCompatLoader());
    }

    private void loadComplete(FMLLoadCompleteEvent event) {
        this.loaded = true;
    }

    /**
     * @return If this mod has been fully loaded. (The {@link FMLLoadCompleteEvent} has been called)
     */
    public boolean isLoaded() {
        return loaded;
    }

    protected ConfigHandlerCommon constructConfigHandler() {
        return new ConfigHandlerForge(this);
    }

    @Override
    public IModHelpersForge getModHelpers() {
        return ModHelpersForge.INSTANCE;
    }

    @Override
    public ICommonProxyCommon getProxy() {
        return proxy;
    }

    @Override
    public ConfigHandlerForge getConfigHandler() {
        return (ConfigHandlerForge) this.configHandler;
    }

    public IEventBus getModEventBus() {
        return modEventBus;
    }

    protected PacketHandlerForge constructPacketHandler() {
        return new PacketHandlerForge(this); // TODO
    }

    @Override
    public PacketHandlerForge getPacketHandlerCommon() {
        return this.packetHandler;
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
        ICommonProxyCommon proxy = getProxy();
        if(proxy != null) {
            proxy.registerEventHooks();
            proxy.registerTickHandlers();
            if (getModHelpers().getMinecraftHelpers().isClientSide()) {
                proxy.registerRenderers();
            }
            proxy.registerPackets(getPacketHandlerCommon());
            getPacketHandlerCommon().init();
        }
    }

    /**
     * Called on the Forge client setup lifecycle event.
     * @param event The setup event.
     */
    protected void setupClient(FMLClientSetupEvent event) {
        // Register proxy things
        ICommonProxyCommon proxy = getProxy();
        if(proxy != null) {
            proxy.registerRenderers();
        }
    }

    /**
     * Load things after Forge registries have been created.
     * @param event The Forge registry creation event.
     */
    private void afterRegistriesCreated(NewRegistryEvent event) {
        getConfigHandler().loadForgeRegistries();
    }

    /**
     * Load things before Forge registries are being filled.
     * @param event The Forge registry filling event.
     */
    private void beforeRegistriedFilled(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.SOUND_EVENTS.getRegistryKey())) {
            // We only need to call this once, and the SOUND_EVENTS event is emitted first.
            getConfigHandler().loadForgeRegistriesFilled();
        }
    }

    protected void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(constructBaseCommand(event.getCommandSelection(), event.getBuildContext()));
    }

    /**
     * Get the mod by id.
     * @param modId The mod id.
     * @return The mod instance or null.
     */
    @Nullable
    public static ModBaseForge get(String modId) {
        return (ModBaseForge) ModBaseCommon.getCommon(modId);
    }

    public static Map<String, ModBaseForge<?>> getMods() {
        return (Map) ModBaseCommon.getCommonMods();
    }

}
