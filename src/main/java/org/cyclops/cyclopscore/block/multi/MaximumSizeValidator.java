package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
        return new TranslatableComponent("multiblock.cyclopscore.error.size.max",
                LocationHelpers.toCompactString(size.offset(1, 1, 1)), LocationHelpers.toCompactString(getMaximumSize().offset(1, 1, 1)));
    }
}
