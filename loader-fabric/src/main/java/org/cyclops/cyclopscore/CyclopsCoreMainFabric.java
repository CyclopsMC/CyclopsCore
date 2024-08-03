package org.cyclops.cyclopscore;

import net.fabricmc.api.ModInitializer;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 *
 */
public class CyclopsCoreMainFabric implements ModInitializer {

    /**
     * The unique instance of this mod.
     */
    public static CyclopsCoreMainFabric _instance;

    private boolean loaded = false;

    public CyclopsCoreMainFabric() {

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
    }
}
