package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * A maximum size validator.
 * @author rubensworks
 */
@Data
public class MaximumSizeValidator implements ISizeValidator {

    private final Vector3i maximumSize;

    @Override
    public ITextComponent isSizeValid(Vector3i size) {
        if(SizeValidators.compareVec3i(size, getMaximumSize()) <= 0) {
            return null;
        }
        return new TranslationTextComponent("multiblock.cyclopscore.error.size.max",
                LocationHelpers.toCompactString(size), LocationHelpers.toCompactString(getMaximumSize()));
    }
}
