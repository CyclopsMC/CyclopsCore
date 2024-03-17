package org.cyclops.cyclopscore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * Block gui component.
 *
 * Pass {@link IBlockContainerProvider#get(BlockState, Level, BlockPos)} ass a lambda to specify the gui.
 *
 * Optionally implement {@link #getOpenStat()} to specify a stat on gui opening.
 *
 * @author rubensworks
 */
public interface IBlockGui {

    public default void writeExtraGuiData(FriendlyByteBuf packetBuffer, Level world, Player player, BlockPos blockPos, InteractionHand hand, BlockHitResult rayTraceResult) {

    }

    /**
     * @return An optional gui opening statistic.
     */
    @Nullable
    public default Stat<ResourceLocation> getOpenStat() {
        return null;
    }

    public static InteractionResult onBlockActivatedHook(IBlockGui block, IBlockContainerProvider blockContainerProvider, BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        // Drop through if the player is sneaking
        if (player.isCrouching()) {
            return InteractionResult.PASS;
        }

        if (!world.isClientSide()) {
            MenuProvider containerProvider = blockContainerProvider.get(blockState, world, blockPos);
            if (containerProvider != null) {
                player.openMenu(containerProvider,
                        packetBuffer -> block.writeExtraGuiData(packetBuffer, world, player, blockPos, hand, rayTraceResult));
                Stat<ResourceLocation> openStat = block.getOpenStat();
                if (openStat != null) {
                    player.awardStat(openStat);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    public static interface IBlockContainerProvider {
        @Nullable
        public MenuProvider get(BlockState blockState, Level world, BlockPos blockPos);
    }

}
