package org.cyclops.cyclopscore.block.multi;

import lombok.Data;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Block;

/**
 * An exact block count validator.
 * @author rubensworks
 */
@Data
public class ExactBlockCountValidator implements IBlockCountValidator {

    private final int exactCount;

    @Override
    public Component isValid(int count, boolean structureComplete, Block block) {
        if(!structureComplete || count == getExactCount()) {
            return null;
        }
        return new TranslatableComponent("multiblock.cyclopscore.error.blockCount.exact",
                getExactCount(), new TranslatableComponent(block.getDescriptionId()), count);
    }
}
