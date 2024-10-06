package org.cyclops.cyclopscore.block.multi;

import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * A minimum size validator.
 *
 * @author rubensworks
 */
public class MinimumSizeValidator implements ISizeValidator {

    private final Vec3i minimumSize;

    public MinimumSizeValidator(Vec3i minimumSize) {
        this.minimumSize = minimumSize;
    }

    @Override
    public Component isSizeValid(Vec3i size) {
        if (SizeValidators.compareVec3i(size, getMinimumSize()) >= 0) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.size.min",
                IModHelpers.get().getLocationHelpers().toCompactString(size.offset(1, 1, 1)), IModHelpers.get().getLocationHelpers().toCompactString(getMinimumSize().offset(1, 1, 1)));
    }

    public Vec3i getMinimumSize() {
        return this.minimumSize;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MinimumSizeValidator)) return false;
        final MinimumSizeValidator other = (MinimumSizeValidator) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$minimumSize = this.getMinimumSize();
        final Object other$minimumSize = other.getMinimumSize();
        if (this$minimumSize == null ? other$minimumSize != null : !this$minimumSize.equals(other$minimumSize))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MinimumSizeValidator;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $minimumSize = this.getMinimumSize();
        result = result * PRIME + ($minimumSize == null ? 43 : $minimumSize.hashCode());
        return result;
    }

    public String toString() {
        return "MinimumSizeValidator(minimumSize=" + this.getMinimumSize() + ")";
    }
}
