package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * A maximum block count validator.
 * @author rubensworks
 */
@Data
public class MaximumBlockCountValidator implements IBlockCountValidator {

    private final int maximumCount;

    @Override
    public ITextComponent isValid(int count, boolean structureComplete, Block block) {
        if(count <= getMaximumCount()) {
            return null;
        }
        return new TranslationTextComponent("multiblock.cyclopscore.error.blockCount.max",
                getMaximumCount(), new TranslationTextComponent(block.getDescriptionId()), count);
    }
}
