package org.cyclops.cyclopscore.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;

import java.util.function.BiFunction;

/**
 * Base block with a block entity and gui.
 *
 * @see BlockWithEntity
 * @see BlockGui
 * @author rubensworks
 */
public class BlockWithEntityGui extends BlockWithEntity implements IBlockGui {

    public BlockWithEntityGui(Properties properties, BiFunction<BlockPos, BlockState, CyclopsBlockEntity> blockEntitySupplier) {
        super(properties, blockEntitySupplier);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        InteractionResult superActivated = super.use(blockState, world, blockPos, player, hand, rayTraceResult);
        if (superActivated.consumesAction()) {
            return superActivated;
        }
        return IBlockGui.onBlockActivatedHook(this, this::getMenuProvider, blockState, world, blockPos, player, hand, rayTraceResult);
    }

}
