package org.cyclops.cyclopscore.persist.nbt;

import net.minecraft.nbt.CompoundTag;

/**
 * Classes tagged with this interface can have their fields persisted to NBT when they are annotated with
 * {@link org.cyclops.cyclopscore.persist.nbt.NBTPersist}.
 * @author rubensworks
 */
public interface INBTProvider {

    /**
     * Write the data in this provider to NBT.
     * @param tag The tag to write to.
     */
    public void writeGeneratedFieldsToNBT(CompoundTag tag);

    /**
     * Read data from the given tag to this provider.
     * @param tag The tag to read from.
     */
    public void readGeneratedFieldsFromNBT(CompoundTag tag);

}
