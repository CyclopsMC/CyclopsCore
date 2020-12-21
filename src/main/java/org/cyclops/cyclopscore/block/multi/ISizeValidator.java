package org.cyclops.cyclopscore.block.multi;

import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;

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
    public ITextComponent isSizeValid(Vector3i size);

}
