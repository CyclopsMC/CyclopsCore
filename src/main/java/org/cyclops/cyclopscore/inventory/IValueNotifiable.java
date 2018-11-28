package org.cyclops.cyclopscore.inventory;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Used for receiving values from servers to clients in guis.
 * @see IValueNotifier
 * @author rubensworks
 */
public interface IValueNotifiable {

    /**
     * @return The mod the gui belongs to.
     */
    String getGuiModId();

    /**
     * @return The id of the gui.
     */
    int getGuiId();

    /**
     * Called by the server if the value has changed.
     * @param valueId The value id.
     * @param value The new value.
     */
    void onUpdate(int valueId, NBTTagCompound value);

}
