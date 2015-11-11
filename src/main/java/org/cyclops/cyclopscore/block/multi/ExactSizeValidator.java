package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.Vec3i;

/**
 * An exact size validator.
 * @author rubensworks
 */
@Data
public class ExactSizeValidator implements ISizeValidator {

    private final Vec3i exactSize;

    @Override
    public boolean isSizeValid(Vec3i size) {
        return SizeValidators.compareVec3i(size, exactSize) == 0;
    }
}
