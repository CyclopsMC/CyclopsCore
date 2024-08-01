package org.cyclops.cyclopscore.persist.world;

import lombok.experimental.Delegate;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
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
     *
     * @param tag                  The tag to read from.
     * @param holderLookupProvider
     */
    public void readFromNBT(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        readGeneratedFieldsFromNBT(tag, holderLookupProvider);
    }

    /**
     * Write the counters.
     *
     * @param tag                  The tag to write to.
     * @param holderLookupProvider
     */
    public void writeToNBT(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        writeGeneratedFieldsToNBT(tag, holderLookupProvider);
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
                new SavedData.Factory<NBTDataHolder>(
                        () -> new NBTDataHolder(this),
                        (t, p) -> NBTDataHolder.load(t, this, server.registryAccess())
                ),
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

        public static NBTDataHolder load(CompoundTag tag, WorldStorage parentStorage, HolderLookup.Provider holderLookupProvider) {
            NBTDataHolder dataHolder = new NBTDataHolder(parentStorage);
            dataHolder.parentStorage.readFromNBT(tag, holderLookupProvider);
            return dataHolder;
        }

        @Override
        public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
            parentStorage.writeToNBT(tag, provider);
            return tag;
        }

        @Override
        public boolean isDirty() {
            // If this proves to be too inefficient, add a decent implementation for this.
            return true;
        }
    }

}
