package org.cyclops.cyclopscore.block.multi;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

/**
 * A minimum block count validator.
 *
 * @author rubensworks
 */
public class MinimumBlockCountValidator implements IBlockCountValidator {

    private final int minimumCount;

    public MinimumBlockCountValidator(int minimumCount) {
        this.minimumCount = minimumCount;
    }

    @Override
    public Component isValid(int count, boolean structureComplete, Block block) {
        if (!structureComplete || count >= getMinimumCount()) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.blockCount.min",
                getMinimumCount(), Component.translatable(block.getDescriptionId()), count);
    }

    public int getMinimumCount() {
        return this.minimumCount;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MinimumBlockCountValidator)) return false;
        final MinimumBlockCountValidator other = (MinimumBlockCountValidator) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getMinimumCount() != other.getMinimumCount()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MinimumBlockCountValidator;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getMinimumCount();
        return result;
    }

    public String toString() {
        return "MinimumBlockCountValidator(minimumCount=" + this.getMinimumCount() + ")";
    }
}
