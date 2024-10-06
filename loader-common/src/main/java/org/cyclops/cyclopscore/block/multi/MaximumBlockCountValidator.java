package org.cyclops.cyclopscore.block.multi;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

/**
 * A maximum block count validator.
 *
 * @author rubensworks
 */
public class MaximumBlockCountValidator implements IBlockCountValidator {

    private final int maximumCount;

    public MaximumBlockCountValidator(int maximumCount) {
        this.maximumCount = maximumCount;
    }

    @Override
    public Component isValid(int count, boolean structureComplete, Block block) {
        if (count <= getMaximumCount()) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.blockCount.max",
                getMaximumCount(), Component.translatable(block.getDescriptionId()), count);
    }

    public int getMaximumCount() {
        return this.maximumCount;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MaximumBlockCountValidator)) return false;
        final MaximumBlockCountValidator other = (MaximumBlockCountValidator) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getMaximumCount() != other.getMaximumCount()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MaximumBlockCountValidator;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getMaximumCount();
        return result;
    }

    public String toString() {
        return "MaximumBlockCountValidator(maximumCount=" + this.getMaximumCount() + ")";
    }
}
