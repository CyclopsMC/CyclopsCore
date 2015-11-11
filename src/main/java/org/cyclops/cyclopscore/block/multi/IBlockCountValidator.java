package org.cyclops.cyclopscore.block.multi;

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
     * @return If the count is valid.
     */
    public boolean isValid(int count, boolean structureComplete);

}
