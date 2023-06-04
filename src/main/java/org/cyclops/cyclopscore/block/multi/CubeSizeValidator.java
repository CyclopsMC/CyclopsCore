package org.cyclops.cyclopscore.block.multi;

import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * A validator to check if all sides have an equal length.
 * @author rubensworks
 */
public class CubeSizeValidator implements ISizeValidator {
    @Override
    public Component isSizeValid(Vec3i size) {
        if(size.getX() == size.getY() && size.getY() == size.getZ()) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.size.cube",
                LocationHelpers.toCompactString(size.offset(1, 1, 1)));
    }
}
