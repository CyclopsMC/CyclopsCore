package org.cyclops.cyclopscore.block.multi;

import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * An exact size validator.
 *
 * @author rubensworks
 */
public class ExactSizeValidator implements ISizeValidator {

    private final Vec3i exactSize;

    public ExactSizeValidator(Vec3i exactSize) {
        this.exactSize = exactSize;
    }

    @Override
    public Component isSizeValid(Vec3i size) {
        if (SizeValidators.compareVec3i(size, getExactSize()) == 0) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.size.exact",
                IModHelpers.get().getLocationHelpers().toCompactString(size.offset(1, 1, 1)), IModHelpers.get().getLocationHelpers().toCompactString(getExactSize().offset(1, 1, 1)));
    }

    public Vec3i getExactSize() {
        return this.exactSize;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ExactSizeValidator)) return false;
        final ExactSizeValidator other = (ExactSizeValidator) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$exactSize = this.getExactSize();
        final Object other$exactSize = other.getExactSize();
        if (this$exactSize == null ? other$exactSize != null : !this$exactSize.equals(other$exactSize)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ExactSizeValidator;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $exactSize = this.getExactSize();
        result = result * PRIME + ($exactSize == null ? 43 : $exactSize.hashCode());
        return result;
    }

    public String toString() {
        return "ExactSizeValidator(exactSize=" + this.getExactSize() + ")";
    }
}
