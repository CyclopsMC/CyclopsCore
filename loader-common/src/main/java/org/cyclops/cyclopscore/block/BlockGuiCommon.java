package org.cyclops.cyclopscore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Block with a gui.
 *
 * Implement {@link IBlockContainerProvider#get(BlockState, Level, BlockPos)} to specify the gui.
 *
 * Optionally implement {@link #getOpenStat()} to specify a stat on gui opening.
 *
 * @author rubensworks
 *
 */
public abstract class BlockGuiCommon extends Block implements IBlockGui {

    public BlockGuiCommon(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level world, BlockPos blockPos, Player player, BlockHitResult rayTraceResult) {
        InteractionResult superActivated = super.useWithoutItem(blockState, world, blockPos, player, rayTraceResult);
        if (superActivated.consumesAction()) {
            return superActivated;
        }
        return IBlockGui.onBlockActivatedHook(this, this::getMenuProvider, blockState, world, blockPos, player, rayTraceResult);
    }

}
