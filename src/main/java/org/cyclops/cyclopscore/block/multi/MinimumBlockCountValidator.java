package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.block.Block;
import org.cyclops.cyclopscore.helper.L10NHelpers;

/**
 * A minimum block count validator.
 * @author rubensworks
 */
@Data
public class MinimumBlockCountValidator implements IBlockCountValidator {

    private final int minimumCount;

    @Override
    public L10NHelpers.UnlocalizedString isValid(int count, boolean structureComplete, Block block) {
        if(!structureComplete || count >= getMinimumCount()) {
            return null;
        }
        return new L10NHelpers.UnlocalizedString("multiblock.cyclopscore.error.blockCount.min",
                getMinimumCount(), new L10NHelpers.UnlocalizedString(block.getUnlocalizedName() + ".name"), count);
    }
}
