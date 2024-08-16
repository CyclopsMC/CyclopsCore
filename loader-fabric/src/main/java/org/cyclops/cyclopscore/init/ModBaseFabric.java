package org.cyclops.cyclopscore.init;

import net.fabricmc.api.ModInitializer;
import org.cyclops.cyclopscore.config.ConfigHandlerFabric;
import org.cyclops.cyclopscore.helper.IModHelpersFabric;
import org.cyclops.cyclopscore.helper.ModBaseCommon;
import org.cyclops.cyclopscore.helper.ModHelpersFabric;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.modcompat.forgeconfig.ModCompatForgeConfig;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Base class for mods which adds a few convenience methods.
 * @author rubensworks
 */
public abstract class ModBaseFabric<T extends ModBaseFabric<T>> extends ModBaseCommon<T> implements ModInitializer {

    private boolean loaded = false;
    private final ICommonProxyCommon proxy;
    private final ConfigHandlerFabric configHandler;

    public ModBaseFabric(String modId, Consumer<T> instanceSetter) {
        super(modId, instanceSetter);
        this.proxy = getModHelpers().getMinecraftHelpers().isClientSide() ? this.constructClientProxy() : this.constructCommonProxy();
        this.configHandler = constructConfigHandler();

        // Initialize config handler
        this.onConfigsRegister(getConfigHandler());
        getConfigHandler().loadModInit();

        loadModCompats(getModCompatLoader());
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

        // Register proxy things
        ICommonProxyCommon proxy = getProxy();
        if(proxy != null) {
            proxy.registerEventHooks();
            proxy.registerTickHandlers();
            if (getModHelpers().getMinecraftHelpers().isClientSide()) {
                proxy.registerRenderers();
            }
        }
    }

    protected ConfigHandlerFabric constructConfigHandler() {
        return new ConfigHandlerFabric(this);
    }

    @Override
    public IModHelpersFabric getModHelpers() {
        return ModHelpersFabric.INSTANCE;
    }

    @Override
    public ICommonProxyCommon getProxy() {
        return proxy;
    }

    @Override
    public ConfigHandlerFabric getConfigHandler() {
        return this.configHandler;
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        super.loadModCompats(modCompatLoader);

        modCompatLoader.addModCompat(new ModCompatForgeConfig());
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
