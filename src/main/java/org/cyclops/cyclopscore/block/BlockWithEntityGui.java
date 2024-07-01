package org.cyclops.cyclopscore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;

import java.util.function.BiFunction;

/**
 * Base block with a block entity and gui.
 *
 * @see BlockWithEntity
 * @see BlockGui
 * @author rubensworks
 */
public abstract class BlockWithEntityGui extends BlockWithEntity implements IBlockGui {

    public BlockWithEntityGui(Properties properties, BiFunction<BlockPos, BlockState, CyclopsBlockEntity> blockEntitySupplier) {
        super(properties, blockEntitySupplier);
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
