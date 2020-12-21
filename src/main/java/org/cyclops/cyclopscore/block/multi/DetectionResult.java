package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * Multiblock detection result.
 * The `error` field is only available when the size is null, so if the structure was invalid.
 * @author rubensworks
 */
@Data
public class DetectionResult {

    private final Vector3i size;
    private final ITextComponent error;

    public DetectionResult(Vector3i size) {
        this.size = size;
        this.error = null;
    }

    public DetectionResult(ITextComponent error) {
        this.size = LocationHelpers.copyLocation(Vector3i.NULL_VECTOR);
        this.error = error;
    }

    public DetectionResult(String error) {
        this(new StringTextComponent(error));
    }

}
