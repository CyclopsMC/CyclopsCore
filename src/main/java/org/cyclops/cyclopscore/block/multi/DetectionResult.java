package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * Multiblock detection result.
 * The `error` field is only available when the size is null, so if the structure was invalid.
 * @author rubensworks
 */
@Data
public class DetectionResult {

    private final Vec3i size;
    private final Component error;

    public DetectionResult(Vec3i size) {
        this.size = size;
        this.error = null;
    }

    public DetectionResult(Component error) {
        this.size = LocationHelpers.copyLocation(Vec3i.ZERO);
        this.error = error;
    }

    public DetectionResult(String error) {
        this(new TextComponent(error));
    }

}
