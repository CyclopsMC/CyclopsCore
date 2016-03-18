package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.math.Vec3i;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * An exact size validator.
 * @author rubensworks
 */
@Data
public class ExactSizeValidator implements ISizeValidator {

    private final Vec3i exactSize;

    @Override
    public L10NHelpers.UnlocalizedString isSizeValid(Vec3i size) {
        if(SizeValidators.compareVec3i(size, getExactSize()) == 0) {
            return null;
        }
        return new L10NHelpers.UnlocalizedString("multiblock.cyclopscore.error.size.exact",
                LocationHelpers.toCompactString(size), LocationHelpers.toCompactString(getExactSize()));
    }
}
