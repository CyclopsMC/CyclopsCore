package org.cyclops.cyclopscore.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import org.cyclops.cyclopscore.persist.nbt.INBTSerializable;

/**
 * An {@link IInventory} that support NBT persistence.
 * @author rubensworks
 */
public interface INBTInventory extends IInventory, INBTSerializable {

    /**
     * Read inventory data from the given NBT.
     * @param data The NBT data containing inventory data.
     */
    public void read(CompoundNBT data);

    /**
     * Write inventory data to the given NBT.
     * @param data The NBT tag that will receive inventory data.
     */
    public void write(CompoundNBT data);

    /**
     * @return If all slots are empty.
     */
    public boolean isEmpty();

}
