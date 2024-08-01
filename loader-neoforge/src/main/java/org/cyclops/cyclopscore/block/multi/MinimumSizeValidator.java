package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * A minimum size validator.
 * @author rubensworks
 */
@Data
public class MinimumSizeValidator implements ISizeValidator {

    private final Vec3i minimumSize;

    @Override
    public Component isSizeValid(Vec3i size) {
        if(SizeValidators.compareVec3i(size, getMinimumSize()) >= 0) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.size.min",
                LocationHelpers.toCompactString(size.offset(1, 1, 1)), LocationHelpers.toCompactString(getMinimumSize().offset(1, 1, 1)));
    }
}
