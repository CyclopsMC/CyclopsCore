package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * A minimum block count validator.
 * @author rubensworks
 */
@Data
public class MinimumBlockCountValidator implements IBlockCountValidator {

    private final int minimumCount;

    @Override
    public ITextComponent isValid(int count, boolean structureComplete, Block block) {
        if(!structureComplete || count >= getMinimumCount()) {
            return null;
        }
        return new TranslationTextComponent("multiblock.cyclopscore.error.blockCount.min",
                getMinimumCount(), new TranslationTextComponent(block.getTranslationKey()), count);
    }
}
