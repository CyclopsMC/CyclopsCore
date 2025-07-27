package org.cyclops.cyclopscore.persist.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * Instances of this can store data inside the world NBT.
 *
 * @author rubensworks
 */
public abstract class WorldStorage<T extends WorldStorage> extends SavedData {

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

    public static abstract class Access<T extends WorldStorage<T>> {

        private final SavedDataType<T> savedDataType;
        protected final ModBaseNeoForge<?> mod;

        public Access(SavedDataType<T> savedDataType, ModBaseNeoForge<?> mod) {
            this.savedDataType = savedDataType;
            this.mod = mod;
        }

        public T get(MinecraftServer server) {
            return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(savedDataType);
        }

        public T get() {
            return get(ServerLifecycleHooks.getCurrentServer());
        }

        /**
         * When a server is started.
         *
         * @param event The received event.
         */
        public void onStartedEvent(ServerStartedEvent event) {
            T data = get(event.getServer());
            data.afterLoad();
        }

        /**
         * When a server is stopping.
         *
         * @param event The received event.
         */
        public void onStoppingEvent(ServerStoppingEvent event) {
            T data = get(event.getServer());
            data.beforeSave();
        }
    }

}
