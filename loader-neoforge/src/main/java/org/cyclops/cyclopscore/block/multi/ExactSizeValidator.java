package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * An exact size validator.
 * @author rubensworks
 */
@Data
public class ExactSizeValidator implements ISizeValidator {

    private final Vec3i exactSize;

    @Override
    public Component isSizeValid(Vec3i size) {
        if(SizeValidators.compareVec3i(size, getExactSize()) == 0) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.size.exact",
                LocationHelpers.toCompactString(size.offset(1, 1, 1)), LocationHelpers.toCompactString(getExactSize().offset(1, 1, 1)));
    }
}
