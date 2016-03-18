package org.cyclops.cyclopscore.block.multi;

import net.minecraft.util.math.Vec3i;
import org.cyclops.cyclopscore.helper.L10NHelpers;

/**
 * Validator for multiblock structure sizes
 * @author rubensworks
 */
public interface ISizeValidator {

    /**
     * Check if the given size is valid.
     * @param size The size to check.
     * @return Null if the size is valid, otherwise the error message.
     */
    public L10NHelpers.UnlocalizedString isSizeValid(Vec3i size);

}
