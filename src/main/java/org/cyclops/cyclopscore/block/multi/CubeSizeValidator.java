package org.cyclops.cyclopscore.block.multi;

import net.minecraft.util.math.Vec3i;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * A validator to check if all sides have an equal length.
 * @author rubensworks
 */
public class CubeSizeValidator implements ISizeValidator {
    @Override
    public L10NHelpers.UnlocalizedString isSizeValid(Vec3i size) {
        if(size.getX() == size.getY() && size.getY() == size.getZ()) {
            return null;
        }
        return new L10NHelpers.UnlocalizedString("multiblock.cyclopscore.error.size.cube",
                LocationHelpers.toCompactString(size));
    }
}
