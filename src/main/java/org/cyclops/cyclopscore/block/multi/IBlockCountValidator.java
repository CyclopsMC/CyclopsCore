package org.cyclops.cyclopscore.block.multi;

import net.minecraft.block.Block;
import net.minecraft.util.text.ITextComponent;

/**
 * Count validator for blocks.
 * This validator will be called multiple times, for each time an allowed block is found.
 * @author rubensworks
 */
public interface IBlockCountValidator {

    /**
     * Check if the block count is valid.
     * @param count The block count up until now.
     * @param structureComplete If this check is done once the structure is fully complete.
     * @param block The block to check, used as reference for the error.
     * @return Null if the location is valid, otherwise the error message.
     */
    public ITextComponent isValid(int count, boolean structureComplete, Block block);

}
