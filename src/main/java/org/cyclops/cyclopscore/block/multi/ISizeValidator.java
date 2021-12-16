package org.cyclops.cyclopscore.block.multi;

import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;

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
    public Component isSizeValid(Vec3i size);

}
