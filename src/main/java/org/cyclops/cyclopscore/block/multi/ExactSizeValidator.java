package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * An exact size validator.
 * @author rubensworks
 */
@Data
public class ExactSizeValidator implements ISizeValidator {

    private final Vector3i exactSize;

    @Override
    public ITextComponent isSizeValid(Vector3i size) {
        if(SizeValidators.compareVec3i(size, getExactSize()) == 0) {
            return null;
        }
        return new TranslationTextComponent("multiblock.cyclopscore.error.size.exact",
                LocationHelpers.toCompactString(size), LocationHelpers.toCompactString(getExactSize()));
    }
}
