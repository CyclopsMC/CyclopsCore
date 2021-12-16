package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Block;

/**
 * A maximum block count validator.
 * @author rubensworks
 */
@Data
public class MaximumBlockCountValidator implements IBlockCountValidator {

    private final int maximumCount;

    @Override
    public Component isValid(int count, boolean structureComplete, Block block) {
        if(count <= getMaximumCount()) {
            return null;
        }
        return new TranslatableComponent("multiblock.cyclopscore.error.blockCount.max",
                getMaximumCount(), new TranslatableComponent(block.getDescriptionId()), count);
    }
}
