package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * An exact block count validator.
 * @author rubensworks
 */
@Data
public class ExactBlockCountValidator implements IBlockCountValidator {

    private final int exactCount;

    @Override
    public ITextComponent isValid(int count, boolean structureComplete, Block block) {
        if(!structureComplete || count == getExactCount()) {
            return null;
        }
        return new TranslationTextComponent("multiblock.cyclopscore.error.blockCount.exact",
                getExactCount(), new TranslationTextComponent(block.getDescriptionId()), count);
    }
}
