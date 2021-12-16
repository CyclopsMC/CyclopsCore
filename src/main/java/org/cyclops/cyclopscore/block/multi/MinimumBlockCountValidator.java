package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
        return new TranslatableComponent("multiblock.cyclopscore.error.blockCount.min",
                getMinimumCount(), new TranslatableComponent(block.getDescriptionId()), count);
    }
}
