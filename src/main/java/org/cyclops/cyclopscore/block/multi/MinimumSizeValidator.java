package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * A minimum size validator.
 * @author rubensworks
 */
@Data
public class MinimumSizeValidator implements ISizeValidator {

    private final Vec3i minimumSize;

    @Override
    public ITextComponent isSizeValid(Vec3i size) {
        if(SizeValidators.compareVec3i(size, getMinimumSize()) >= 0) {
            return null;
        }
        return new TranslationTextComponent("multiblock.cyclopscore.error.size.min",
                LocationHelpers.toCompactString(size), LocationHelpers.toCompactString(getMinimumSize()));
    }
}
