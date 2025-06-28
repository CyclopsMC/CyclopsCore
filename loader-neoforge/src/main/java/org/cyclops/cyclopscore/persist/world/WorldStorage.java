package org.cyclops.cyclopscore.persist.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * Instances of this can store data inside the world NBT.
 *
 * @author rubensworks
 */
public abstract class WorldStorage<T extends WorldStorage> extends SavedData {

    private final SavedDataType<T> savedDataType;
    protected final ModBaseNeoForge mod;

    public WorldStorage(ModBaseNeoForge mod) {
        this.mod = mod;
        this.savedDataType = this.constructSavedDataType();
    }

    /**
     * Reset the stored data because it will be reloaded from NBT.
     */
    public abstract void reset();

    /**
     * When a server is started.
     *
     * @param event The received event.
     */
    public void onAboutToStartEvent(ServerAboutToStartEvent event) {
        reset();
    }

    /**
     * When a server is started.
     *
     * @param event The received event.
     */
    public void onStartedEvent(ServerStartedEvent event) {
        reset();
        initDataHolder(event.getServer());
        afterLoad();
    }

    /**
     * When a server is stopping.
     *
     * @param event The received event.
     */
    public void onStoppingEvent(ServerStoppingEvent event) {
        beforeSave();
        initDataHolder(event.getServer());
    }

    protected abstract SavedDataType<T> constructSavedDataType();

    private T initDataHolder(MinecraftServer server) {
        return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(this.savedDataType);
    }

    /**
     * Called after the data is loaded from the world storage.
     */
    public void afterLoad() {

    }

    /**
     * Called before the data is saved to the world storage.
     */
    public void beforeSave() {

    }

}
