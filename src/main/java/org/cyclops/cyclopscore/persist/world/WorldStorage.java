package org.cyclops.cyclopscore.persist.world;

import lombok.experimental.Delegate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSavedData;
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
    public void onStartedEvent(FMLServerStartedEvent event) {
        loadData();
    }

    /**
     * When a server is stopping.
     * @param event The received event.
     */
    public void onStoppingEvent(FMLServerStoppingEvent event) {
        saveData();
    }

    protected abstract String getDataId();

    private NBTDataHolder getDataHolder() {
        String dataId = mod.getModId() + "_" + getDataId();
        NBTDataHolder data = (NBTDataHolder) MinecraftServer.getServer().worldServers[0].
                loadItemData(NBTDataHolder.class, dataId);
        if(data == null) {
            data = new NBTDataHolder(dataId);
            MinecraftServer.getServer().worldServers[0].setItemData(dataId, data);
        }
        return data;
    }

    private synchronized void loadData() {
        reset();
        readFromNBT(getDataHolder().tag);
    }

    private synchronized void saveData() {
        NBTDataHolder data = getDataHolder();
        writeToNBT(data.tag);
        data.setDirty(true);
    }

    /**
     * Data holder for the global counter data.
     */
    public static class NBTDataHolder extends WorldSavedData {

        private static String KEY = "WorldStorageData";

        public NBTTagCompound tag;

        /**
         * Make a new instance.
         * @param key The key for the global counter data.
         */
        public NBTDataHolder(String key) {
            super(key);
            this.tag = new NBTTagCompound();
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            this.tag = tag.getCompoundTag(KEY);
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setTag(KEY, this.tag);
        }

    }

}
