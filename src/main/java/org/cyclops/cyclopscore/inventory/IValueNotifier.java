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
     * If called on the client, a packet to the server will be sent.
     * If called on the server, a packet to the client will be sent.
     * @param valueId The value id.
     * @param value The new value.
     */
    void setValue(int valueId, NBTTagCompound value);

}
