package org.cyclops.cyclopscore.init;

import net.fabricmc.api.ModInitializer;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.helper.ConfigHandlerFabric;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.ModBaseCommon;
import org.cyclops.cyclopscore.helper.ModHelpersFabric;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Base class for mods which adds a few convenience methods.
 * @author rubensworks
 */
public class ModBaseFabric<T extends ModBaseFabric<T>> extends ModBaseCommon<T> implements ModInitializer {

    private boolean loaded = false;
    private final ConfigHandler configHandler;

    public ModBaseFabric(String modId, Consumer<T> instanceSetter) {
        super(modId, instanceSetter);
        this.configHandler = constructConfigHandler();

        // Initialize config handler
        this.onConfigsRegister(getConfigHandler());
        getConfigHandler().loadModInit();
    }

    /**
     * @return If this mod has been fully loaded. ({@link #onInitialize()} has been called)
     */
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public void onInitialize() {
        this.loaded = true;

        getConfigHandler().loadSetup();
        getConfigHandler().loadForgeRegistries();
        getConfigHandler().loadForgeRegistriesFilled();
    }

    protected ConfigHandler constructConfigHandler() {
        return new ConfigHandlerFabric(this);
    }

    @Override
    public IModHelpers getModHelpers() {
        return ModHelpersFabric.INSTANCE;
    }

    @Override
    public ConfigHandler getConfigHandler() {
        return this.configHandler;
    }

    /**
     * Get the mod by id.
     * @param modId The mod id.
     * @return The mod instance or null.
     */
    @Nullable
    public static ModBaseFabric get(String modId) {
        return (ModBaseFabric) ModBaseCommon.getCommon(modId);
    }

    public static Map<String, ModBaseFabric<?>> getMods() {
        return (Map) ModBaseCommon.getCommonMods();
    }
}
