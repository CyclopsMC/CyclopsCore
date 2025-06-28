package org.cyclops.cyclopscore.persist.nbt;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Classes tagged with this interface can have their fields persisted to NBT when they are annotated with
 * {@link org.cyclops.cyclopscore.persist.nbt.NBTPersist}.
 * @author rubensworks
 */
public interface INBTProvider {

    /**
     * Write the data in this provider to NBT.
     *
     * @param output The tag to write to.
     */
    public void writeGeneratedFieldsToNBT(ValueOutput output);

    /**
     * Read data from the given tag to this provider.
     *
     * @param input The tag to read from.
     */
    public void readGeneratedFieldsFromNBT(ValueInput input);

}
