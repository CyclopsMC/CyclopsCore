package org.cyclops.cyclopscore.block.multi;

import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * Multiblock detection result.
 * The `error` field is only available when the size is null, so if the structure was invalid.
 *
 * @author rubensworks
 */
public class DetectionResult {

    private final Vec3i size;
    private final Component error;

    public DetectionResult(Vec3i size) {
        this.size = size;
        this.error = null;
    }

    public DetectionResult(Component error) {
        this.size = IModHelpers.get().getLocationHelpers().copyLocation(Vec3i.ZERO);
        this.error = error;
    }

    public DetectionResult(String error) {
        this(Component.literal(error));
    }

    public Vec3i getSize() {
        return this.size;
    }

    public Component getError() {
        return this.error;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DetectionResult)) return false;
        final DetectionResult other = (DetectionResult) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$size = this.getSize();
        final Object other$size = other.getSize();
        if (this$size == null ? other$size != null : !this$size.equals(other$size)) return false;
        final Object this$error = this.getError();
        final Object other$error = other.getError();
        if (this$error == null ? other$error != null : !this$error.equals(other$error)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DetectionResult;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $size = this.getSize();
        result = result * PRIME + ($size == null ? 43 : $size.hashCode());
        final Object $error = this.getError();
        result = result * PRIME + ($error == null ? 43 : $error.hashCode());
        return result;
    }

    public String toString() {
        return "DetectionResult(size=" + this.getSize() + ", error=" + this.getError() + ")";
    }
}
