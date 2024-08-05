package org.cyclops.cyclopscore.init;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.ConfigHandlerForge;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.ModBaseCommon;
import org.cyclops.cyclopscore.helper.ModHelpersForge;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Base class for mods which adds a few convenience methods.
 * @author rubensworks
 */
public class ModBaseForge<T extends ModBaseForge<T>> extends ModBaseCommon<T> {

    private final ConfigHandler configHandler;
    private final IEventBus modEventBus;

    private boolean loaded = false;

    public ModBaseForge(String modId, Consumer<T> instanceSetter) {
        super(modId, instanceSetter);
        this.modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        this.configHandler = constructConfigHandler();

        // Register listeners
        getModEventBus().addListener(this::setup);
        getModEventBus().addListener(EventPriority.LOWEST, this::afterRegistriesCreated);
        getModEventBus().addListener(EventPriority.HIGHEST, this::beforeRegistriedFilled);
        getModEventBus().addListener(this::loadComplete);

        // Initialize config handler
        this.onConfigsRegister(getConfigHandler());
        getConfigHandler().initialize(Lists.newArrayList());
        getConfigHandler().loadModInit();
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

    protected ConfigHandler constructConfigHandler() {
        return new ConfigHandlerForge(this);
    }

    @Override
    public IModHelpers getModHelpers() {
        return ModHelpersForge.INSTANCE;
    }

    @Override
    public ConfigHandlerForge getConfigHandler() {
        return (ConfigHandlerForge) this.configHandler;
    }

    public IEventBus getModEventBus() {
        return modEventBus;
    }

    /**
     * Called on the Forge setup lifecycle event.
     * @param event The setup event.
     */
    protected void setup(FMLCommonSetupEvent event) {
        log(Level.TRACE, "setup()");

        // Iterate over all configs again
        getConfigHandler().loadSetup();
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
        if (event.getRegistryKey().equals(BuiltInRegistries.ATTRIBUTE.key())) {
            // We only need to call this once, and the ATTRIBUTE event is emitted first.
            getConfigHandler().loadForgeRegistriesFilled();
        }
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
