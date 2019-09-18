package org.cyclops.cyclopscore.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.Stat;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Block gui component.
 *
 * Implement {@link #getContainer(BlockState, World, BlockPos)} to specify the gui.
 *
 * Optionally implement {@link #getOpenStat()} to specify a stat on gui opening.
 *
 * @author rubensworks
 */
public interface IBlockGui {

    public default void writeExtraGuiData(PacketBuffer packetBuffer, World world, PlayerEntity player, BlockPos blockPos, Hand hand, BlockRayTraceResult rayTraceResult) {

    }

    /**
     * @return An optional gui opening statistic.
     */
    @Nullable
    public default Stat<ResourceLocation> getOpenStat() {
        return null;
    }

    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos);

    public static boolean onBlockActivatedHook(IBlockGui block, BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        // Drop through if the player is sneaking
        if (player.isSneaking()) {
            return false;
        }

        if (!world.isRemote()) {
            INamedContainerProvider containerProvider = block.getContainer(blockState, world, blockPos);
            if (containerProvider != null) {
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider,
                        packetBuffer -> block.writeExtraGuiData(packetBuffer, world, player, blockPos, hand, rayTraceResult));
                Stat<ResourceLocation> openStat = block.getOpenStat();
                if (openStat != null) {
                    player.addStat(openStat);
                }
            }
        }

        return true;
    }

}
