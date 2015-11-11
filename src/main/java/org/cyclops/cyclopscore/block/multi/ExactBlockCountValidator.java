package org.cyclops.cyclopscore.block.multi;

import lombok.Data;

/**
 * An exact block count validator.
 * @author rubensworks
 */
@Data
public class ExactBlockCountValidator implements IBlockCountValidator {

    private final int exactCount;

    @Override
    public boolean isValid(int count, boolean structureComplete) {
        return !structureComplete || count == exactCount;
    }
}
