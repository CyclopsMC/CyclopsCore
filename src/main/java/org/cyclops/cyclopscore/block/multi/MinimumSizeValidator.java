package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.math.Vec3i;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * A minimum size validator.
 * @author rubensworks
 */
@Data
public class MinimumSizeValidator implements ISizeValidator {

    private final Vec3i minimumSize;

    @Override
    public L10NHelpers.UnlocalizedString isSizeValid(Vec3i size) {
        if(SizeValidators.compareVec3i(size, getMinimumSize()) >= 0) {
            return null;
        }
        return new L10NHelpers.UnlocalizedString("multiblock.cyclopscore.error.size.min",
                LocationHelpers.toCompactString(size), LocationHelpers.toCompactString(getMinimumSize()));
    }
}
