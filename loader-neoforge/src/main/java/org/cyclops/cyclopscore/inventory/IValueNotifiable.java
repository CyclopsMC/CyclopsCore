package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.nbt.CompoundTag;

/**
 * Used for receiving values from servers to clients in guis.
 * @see IValueNotifier
 * @author rubensworks
 */
public interface IValueNotifiable {

    /**
     * @return The container type.
     */
    public MenuType<?> getValueNotifiableType();

    /**
     * Called by the server if the value has changed.
     * @param valueId The value id.
     * @param value The new value.
     */
    void onUpdate(int valueId, CompoundTag value);

}
