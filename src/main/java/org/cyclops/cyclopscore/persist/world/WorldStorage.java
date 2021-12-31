package org.cyclops.cyclopscore.persist.world;

import lombok.experimental.Delegate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.cyclopscore.persist.nbt.NBTProviderComponent;

/**
 * Instances of this can store data inside the world NBT.
 * @author rubensworks
 */
public abstract class WorldStorage implements INBTProvider {

    protected final ModBase mod;
    @Delegate
    private INBTProvider nbtProviderComponent = new NBTProviderComponent(this);

    public WorldStorage(ModBase mod) {
        this.mod = mod;
    }

    /**
     * Read the counters.
     * @param tag The tag to read from.
     */
    public void readFromNBT(CompoundTag tag) {
        readGeneratedFieldsFromNBT(tag);
    }

    /**
     * Write the counters.
     * @param tag The tag to write to.
     */
    public void writeToNBT(CompoundTag tag) {
        writeGeneratedFieldsToNBT(tag);
    }

    /**
     * Reset the stored data because it will be reloaded from NBT.
     */
    public abstract void reset();

    /**
     * When a server is started.
     * @param event The received event.
     */
    public void onAboutToStartEvent(ServerAboutToStartEvent event) {
        reset();
    }

    /**
     * When a server is started.
     * @param event The received event.
     */
    public void onStartedEvent(ServerStartedEvent event) {
        reset();
        initDataHolder(event.getServer());
        afterLoad();
    }

    /**
     * When a server is stopping.
     * @param event The received event.
     */
    public void onStoppingEvent(ServerStoppingEvent event) {
        beforeSave();
        initDataHolder(event.getServer());
    }

    protected abstract String getDataId();

    private NBTDataHolder initDataHolder(MinecraftServer server) {
        return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(
                e -> NBTDataHolder.load(e, this),
                () -> new NBTDataHolder(this),
                mod.getModId() + "_" + getDataId());
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

    /**
     * Data holder for the global counter data.
     */
    public static class NBTDataHolder extends SavedData {

        private final WorldStorage parentStorage;

        public NBTDataHolder(WorldStorage parentStorage) {
            this.parentStorage = parentStorage;
        }

        public static NBTDataHolder load(CompoundTag tag, WorldStorage parentStorage) {
            NBTDataHolder dataHolder = new NBTDataHolder(parentStorage);
            dataHolder.parentStorage.readFromNBT(tag);
            return dataHolder;
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            parentStorage.writeToNBT(tag);
            return tag;
        }

        @Override
        public boolean isDirty() {
            // If this proves to be too inefficient, add a decent implementation for this.
            return true;
        }
    }

}
