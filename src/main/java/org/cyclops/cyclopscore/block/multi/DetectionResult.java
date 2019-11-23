package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * Multiblock detection result.
 * The `error` field is only available when the size is null, so if the structure was invalid.
 * @author rubensworks
 */
@Data
public class DetectionResult {

    private final Vec3i size;
    private final ITextComponent error;

    public DetectionResult(Vec3i size) {
        this.size = size;
        this.error = null;
    }

    public DetectionResult(ITextComponent error) {
        this.size = LocationHelpers.copyLocation(Vec3i.NULL_VECTOR);
        this.error = error;
    }

    public DetectionResult(String error) {
        this(new StringTextComponent(error));
    }

}
