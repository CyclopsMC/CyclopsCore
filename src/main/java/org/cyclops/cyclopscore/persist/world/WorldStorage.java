package org.cyclops.cyclopscore.persist.world;

import lombok.experimental.Delegate;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.cyclopscore.persist.nbt.NBTProviderComponent;

/**
 * Instances of this can store data inside the world NBT.
 * @author rubensworks
 */
public abstract class WorldStorage implements INBTProvider {

    private static String KEY = "WorldStorageData";

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
    public void readFromNBT(CompoundNBT tag) {
        readGeneratedFieldsFromNBT(tag);
    }

    /**
     * Write the counters.
     * @param tag The tag to write to.
     */
    public void writeToNBT(CompoundNBT tag) {
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
    public void onAboutToStartEvent(FMLServerAboutToStartEvent event) {
        reset();
    }

    /**
     * When a server is started.
     * @param event The received event.
     */
    public void onStartedEvent(FMLServerStartedEvent event) {
        reset();
        loadData(event.getServer());
        afterLoad();
    }

    /**
     * When a server is stopping.
     * @param event The received event.
     */
    public void onStoppingEvent(FMLServerStoppingEvent event) {
        beforeSave();
        saveData(event.getServer());
    }

    protected abstract String getDataId();

    private NBTDataHolder initDataHolder(MinecraftServer server, boolean loading) {
        String dataId = mod.getModId() + "_" + getDataId();
        NBTDataHolder data = server.getLevel(World.OVERWORLD).getDataStorage()
                .computeIfAbsent(() -> new NBTDataHolder(dataId), dataId);
        if (loading) {
            CompoundNBT tempTag = data.getTempTagAndReset();
            if (tempTag != null) {
                readFromNBT(tempTag);
            }
        }
        data.setParentStorage(this);
        return data;
    }

    private synchronized void loadData(MinecraftServer server) {
        initDataHolder(server, true);
    }

    private synchronized void saveData(MinecraftServer server) {
        initDataHolder(server, false);
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
    public static class NBTDataHolder extends WorldSavedData {

        private WorldStorage parentStorage = null;
        private CompoundNBT tempTag = null;

        /**
         * Make a new instance.
         * @param key The key for the global counter data.
         */
        public NBTDataHolder(String key) {
            super(key);
        }

        @Override
        public void load(CompoundNBT tag) {
            if (parentStorage == null) {
                this.tempTag = tag.getCompound(KEY);
            } else {
                parentStorage.readFromNBT(tag.getCompound(KEY));
            }
        }

        @Override
        public CompoundNBT save(CompoundNBT tag) {
            CompoundNBT dataTag = new CompoundNBT();
            parentStorage.writeToNBT(dataTag);
            tag.put(KEY, dataTag);
            return tag;
        }

        @Override
        public boolean isDirty() {
            // If this proves to be too inefficient, add a decent implementation for this.
            return true;
        }

        public void setParentStorage(WorldStorage parentStorage) {
            this.parentStorage = parentStorage;
        }

        public CompoundNBT getTempTagAndReset() {
            CompoundNBT tempTag = this.tempTag;
            this.tempTag = null;
            return tempTag;
        }
    }

}
