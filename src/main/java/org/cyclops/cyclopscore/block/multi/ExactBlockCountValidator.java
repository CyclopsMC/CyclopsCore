package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.block.Block;
import org.cyclops.cyclopscore.helper.L10NHelpers;

/**
 * An exact block count validator.
 * @author rubensworks
 */
@Data
public class ExactBlockCountValidator implements IBlockCountValidator {

    private final int exactCount;

    @Override
    public L10NHelpers.UnlocalizedString isValid(int count, boolean structureComplete, Block block) {
        if(!structureComplete || count == getExactCount()) {
            return null;
        }
        return new L10NHelpers.UnlocalizedString("multiblock.cyclopscore.error.blockCount.exact",
                getExactCount(), new L10NHelpers.UnlocalizedString(block.getUnlocalizedName() + ".name"), count);
    }
}
