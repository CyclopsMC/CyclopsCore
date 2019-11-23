package org.cyclops.cyclopscore.block.multi;

import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * A validator to check if all sides have an equal length.
 * @author rubensworks
 */
public class CubeSizeValidator implements ISizeValidator {
    @Override
    public ITextComponent isSizeValid(Vec3i size) {
        if(size.getX() == size.getY() && size.getY() == size.getZ()) {
            return null;
        }
        return new TranslationTextComponent("multiblock.cyclopscore.error.size.cube",
                LocationHelpers.toCompactString(size));
    }
}
