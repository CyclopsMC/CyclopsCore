package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.block.Block;
import org.cyclops.cyclopscore.helper.L10NHelpers;

/**
 * A maximum block count validator.
 * @author rubensworks
 */
@Data
public class MaximumBlockCountValidator implements IBlockCountValidator {

    private final int maximumCount;

    @Override
    public L10NHelpers.UnlocalizedString isValid(int count, boolean structureComplete, Block block) {
        if(count <= getMaximumCount()) {
            return null;
        }
        return new L10NHelpers.UnlocalizedString("multiblock.cyclopscore.error.blockCount.max",
                getMaximumCount(), new L10NHelpers.UnlocalizedString(block.getTranslationKey() + ".name"), count);
    }
}
