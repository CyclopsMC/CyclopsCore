package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.Container;
import net.minecraft.nbt.CompoundTag;
import org.cyclops.cyclopscore.persist.nbt.INBTSerializable;

/**
 * An {@link Container} that support NBT persistence.
 * @author rubensworks
 */
public interface INBTInventory extends Container, INBTSerializable {

    /**
     * Read inventory data from the given NBT.
     * @param data The NBT data containing inventory data.
     */
    public void read(CompoundTag data);

    /**
     * Write inventory data to the given NBT.
     * @param data The NBT tag that will receive inventory data.
     */
    public void write(CompoundTag data);

    /**
     * @return If all slots are empty.
     */
    public boolean isEmpty();

}
