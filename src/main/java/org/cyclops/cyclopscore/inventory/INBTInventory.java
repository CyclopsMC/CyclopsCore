package org.cyclops.cyclopscore.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

/**
 * An {@link IInventory} that support NBT persistence.
 * @author rubensworks
 */
public interface INBTInventory extends IInventory {

    /**
     * Read inventory data from the given NBT.
     * @param data The NBT data containing inventory data.
     */
    public void readFromNBT(NBTTagCompound data);

    /**
     * Write inventory data to the given NBT.
     * @param data The NBT tag that will receive inventory data.
     */
    public void writeToNBT(NBTTagCompound data);

    /**
     * @return If all slots are empty.
     */
    public boolean isEmpty();

}
