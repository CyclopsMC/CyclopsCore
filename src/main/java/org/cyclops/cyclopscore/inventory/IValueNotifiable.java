package org.cyclops.cyclopscore.inventory;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;

/**
 * Used for receiving values from servers to clients in guis.
 * @see IValueNotifier
 * @author rubensworks
 */
public interface IValueNotifiable {

    /**
     * @return The container type.
     */
    public ContainerType<?> getType();

    /**
     * Called by the server if the value has changed.
     * @param valueId The value id.
     * @param value The new value.
     */
    void onUpdate(int valueId, CompoundNBT value);

}
