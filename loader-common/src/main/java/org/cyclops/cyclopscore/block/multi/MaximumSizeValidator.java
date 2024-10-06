package org.cyclops.cyclopscore.block.multi;

import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * A maximum size validator.
 *
 * @author rubensworks
 */
public class MaximumSizeValidator implements ISizeValidator {

    private final Vec3i maximumSize;

    public MaximumSizeValidator(Vec3i maximumSize) {
        this.maximumSize = maximumSize;
    }

    @Override
    public Component isSizeValid(Vec3i size) {
        if (SizeValidators.compareVec3i(size, getMaximumSize()) <= 0) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.size.max",
                IModHelpers.get().getLocationHelpers().toCompactString(size), IModHelpers.get().getLocationHelpers().toCompactString(getMaximumSize().offset(1, 1, 1)));
    }

    public Vec3i getMaximumSize() {
        return this.maximumSize;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MaximumSizeValidator)) return false;
        final MaximumSizeValidator other = (MaximumSizeValidator) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$maximumSize = this.getMaximumSize();
        final Object other$maximumSize = other.getMaximumSize();
        if (this$maximumSize == null ? other$maximumSize != null : !this$maximumSize.equals(other$maximumSize))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MaximumSizeValidator;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $maximumSize = this.getMaximumSize();
        result = result * PRIME + ($maximumSize == null ? 43 : $maximumSize.hashCode());
        return result;
    }

    public String toString() {
        return "MaximumSizeValidator(maximumSize=" + this.getMaximumSize() + ")";
    }
}
