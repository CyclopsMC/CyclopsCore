package org.cyclops.cyclopscore.block.multi;

import lombok.Data;

/**
 * A maximum block count validator.
 * @author rubensworks
 */
@Data
public class MaximumBlockCountValidator implements IBlockCountValidator {

    private final int maximumCount;

    @Override
    public boolean isValid(int count, boolean structureComplete) {
        return count <= maximumCount;
    }
}
