package org.cyclops.cyclopscore.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * Block with a gui.
 *
 * Implement {@link #getContainer(BlockState, World, BlockPos)} to specify the gui.
 *
 * Optionally implement {@link #getOpenStat()} to specify a stat on gui opening.
 *
 * @author rubensworks
 *
 */
public abstract class BlockGui extends Block implements IBlockGui {

    public BlockGui(Block.Properties properties) {
        super(properties);
    }

    @Override
    public boolean onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (super.onBlockActivated(blockState, world, blockPos, player, hand, rayTraceResult)) {
            return true;
        }
        return IBlockGui.onBlockActivatedHook(this, this::getContainer, blockState, world, blockPos, player, hand, rayTraceResult);
    }

}
