package org.cyclops.cyclopscore.inventory;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Used for sending values from server to clients in guis.
 * Similar to Minecraft's ICrafting.
 * @see IValueNotifiable
 * @author rubensworks
 */
public interface IValueNotifier {

    /**
     * Set the value for given value id.
     * This will send a packet if it has changed.
     * @param valueId The value id.
     * @param value The new value.
     */
    void setValue(int valueId, NBTTagCompound value);

}
