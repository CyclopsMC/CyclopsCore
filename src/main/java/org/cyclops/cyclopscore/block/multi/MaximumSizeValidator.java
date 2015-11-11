package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.Vec3i;

/**
 * A maximum size validator.
 * @author rubensworks
 */
@Data
public class MaximumSizeValidator implements ISizeValidator {

    private final Vec3i maximumSize;

    @Override
    public boolean isSizeValid(Vec3i size) {
        return SizeValidators.compareVec3i(size, maximumSize) <= 0;
    }
}
