package org.cyclops.cyclopscore.persist.world;

import lombok.experimental.Delegate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
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
    public void readFromNBT(NBTTagCompound tag) {
        readGeneratedFieldsFromNBT(tag);
    }

    /**
     * Write the counters.
     * @param tag The tag to write to.
     */
    public void writeToNBT(NBTTagCompound tag) {
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
        loadData();
        afterLoad();
    }

    /**
     * When a server is stopping.
     * @param event The received event.
     */
    public void onStoppingEvent(FMLServerStoppingEvent event) {
        beforeSave();
        saveData();
    }

    protected abstract String getDataId();

    private NBTDataHolder initDataHolder(boolean loading) {
        String dataId = mod.getModId() + "_" + getDataId();
        NBTDataHolder data = (NBTDataHolder) FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0].
                loadData(NBTDataHolder.class, dataId);
        if(data == null) {
            data = new NBTDataHolder(dataId);
            FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0].setData(dataId, data);
        } else if (loading) {
            NBTTagCompound tempTag = data.getTempTagAndReset();
            if (tempTag != null) {
                readFromNBT(tempTag);
            }
        }
        data.setParentStorage(this);
        return data;
    }

    private synchronized void loadData() {
        initDataHolder(true);
    }

    private synchronized void saveData() {
        initDataHolder(false);
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
        private NBTTagCompound tempTag = null;

        /**
         * Make a new instance.
         * @param key The key for the global counter data.
         */
        public NBTDataHolder(String key) {
            super(key);
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            if (parentStorage == null) {
                this.tempTag = tag.getCompoundTag(KEY);
            } else {
                parentStorage.readFromNBT(tag.getCompoundTag(KEY));
            }
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            NBTTagCompound dataTag = new NBTTagCompound();
            parentStorage.writeToNBT(dataTag);
            tag.setTag(KEY, dataTag);
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

        public NBTTagCompound getTempTagAndReset() {
            NBTTagCompound tempTag = this.tempTag;
            this.tempTag = null;
            return tempTag;
        }
    }

}
