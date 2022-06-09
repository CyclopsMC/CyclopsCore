package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

/**
 * A minimum block count validator.
 * @author rubensworks
 */
@Data
public class MinimumBlockCountValidator implements IBlockCountValidator {

    private final int minimumCount;

    @Override
    public Component isValid(int count, boolean structureComplete, Block block) {
        if(!structureComplete || count >= getMinimumCount()) {
            return null;
        }
        return Component.translatable("multiblock.cyclopscore.error.blockCount.min",
                getMinimumCount(), Component.translatable(block.getDescriptionId()), count);
    }
}
