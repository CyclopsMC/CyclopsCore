package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.util.Vec3i;
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
    private final L10NHelpers.UnlocalizedString error;

    public DetectionResult(Vec3i size) {
        this.size = size;
        this.error = null;
    }

    public DetectionResult(L10NHelpers.UnlocalizedString error) {
        this.size = LocationHelpers.copyLocation(Vec3i.NULL_VECTOR);
        this.error = error;
    }

    public DetectionResult(String error) {
        this(new L10NHelpers.UnlocalizedString(error));
    }

}
