package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * A maximum size validator.
 * @author rubensworks
 */
@Data
public class MaximumSizeValidator implements ISizeValidator {

    private final Vec3i maximumSize;

    @Override
    public Component isSizeValid(Vec3i size) {
        if(SizeValidators.compareVec3i(size, getMaximumSize()) <= 0) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.size.max",
                LocationHelpers.toCompactString(size), LocationHelpers.toCompactString(getMaximumSize()));
    }
}
