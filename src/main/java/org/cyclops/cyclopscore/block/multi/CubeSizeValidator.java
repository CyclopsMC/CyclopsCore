package org.cyclops.cyclopscore.block.multi;

import net.minecraft.util.Vec3i;

/**
 * A validator to check if all sides have an equal length.
 * @author rubensworks
 */
public class CubeSizeValidator implements ISizeValidator {
    @Override
    public boolean isSizeValid(Vec3i size) {
        return size.getX() == size.getY() && size.getY() == size.getZ();
    }
}
