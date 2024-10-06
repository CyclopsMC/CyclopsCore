package org.cyclops.cyclopscore.block.multi;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

/**
 * An exact block count validator.
 *
 * @author rubensworks
 */
public class ExactBlockCountValidator implements IBlockCountValidator {

    private final int exactCount;

    public ExactBlockCountValidator(int exactCount) {
        this.exactCount = exactCount;
    }

    @Override
    public Component isValid(int count, boolean structureComplete, Block block) {
        if (!structureComplete || count == getExactCount()) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.blockCount.exact",
                getExactCount(), Component.translatable(block.getDescriptionId()), count);
    }

    public int getExactCount() {
        return this.exactCount;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ExactBlockCountValidator)) return false;
        final ExactBlockCountValidator other = (ExactBlockCountValidator) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getExactCount() != other.getExactCount()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ExactBlockCountValidator;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getExactCount();
        return result;
    }

    public String toString() {
        return "ExactBlockCountValidator(exactCount=" + this.getExactCount() + ")";
    }
}
