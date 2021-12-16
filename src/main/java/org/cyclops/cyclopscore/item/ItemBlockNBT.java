package org.cyclops.cyclopscore.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.block.BlockWithEntity;

import javax.annotation.Nullable;

/**
 * An extended {@link BlockItem} that will add the NBT data that is stored inside
 * the item to the placed {@link BlockEntity} for the blockState.
 * Subinstances of {@link BlockWithEntity} will perform the inverse operation, being
 * that broken blocks will save the NBT data inside the dropped {@link BlockItem}.
 * @author rubensworks
 *
 */
public class ItemBlockNBT extends BlockItem {

    /**
     * Make a new instance.
     * @param block The blockState instance.
     * @param builder Item properties builder.
     */
    public ItemBlockNBT(Block block, Item.Properties builder) {
        super(block, builder);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level world, @Nullable Player player, ItemStack itemStack, BlockState blockState) {
        if (super.updateCustomBlockEntityTag(pos, world, player, itemStack, blockState)) {
            return true;
        }

        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null) {
            if (!world.isClientSide() && tile.onlyOpCanSetNbt() && (player == null || !player.canUseGameMasterBlocks())) {
                return false;
            }
            return itemStackDataToTile(itemStack, tile);
        }

        return false;
    }

    /**
     * Read additional info about the item into the tile.
     * @param tile The tile that is being created.
     * @param itemStack The item that is placed.
     * @return If the tile was changed.
     */
    protected boolean itemStackDataToTile(ItemStack itemStack, BlockEntity tile) {
    	return false;
    }

}
