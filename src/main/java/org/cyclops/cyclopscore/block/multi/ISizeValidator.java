package org.cyclops.cyclopscore.block.multi;

import net.minecraft.util.Vec3i;

/**
 * Validator for multiblock structure sizes
 * @author rubensworks
 */
public interface ISizeValidator {

    public boolean isSizeValid(Vec3i size);

}
