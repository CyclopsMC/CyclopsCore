package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.Vec3i;

/**
 * A minimum size validator.
 * @author rubensworks
 */
@Data
public class MinimumSizeValidator implements ISizeValidator {

    private final Vec3i minimumSize;

    @Override
    public boolean isSizeValid(Vec3i size) {
        return SizeValidators.compareVec3i(size, minimumSize) >= 0;
    }
}
