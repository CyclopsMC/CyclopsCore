package org.cyclops.cyclopscore.block.multi;

import lombok.Data;

/**
 * A minimum block count validator.
 * @author rubensworks
 */
@Data
public class MinimumBlockCountValidator implements IBlockCountValidator {

    private final int minimumCount;

    @Override
    public boolean isValid(int count, boolean structureComplete) {
        return !structureComplete || count >= minimumCount;
    }
}
