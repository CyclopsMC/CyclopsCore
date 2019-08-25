package org.cyclops.cyclopscore.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.CyclopsBlock;

import javax.annotation.Nullable;

/**
 * An extended {@link ItemBlockMetadata} that will add the NBT data that is stored inside
 * the item to the placed {@link TileEntity} for the blockState.
 * Subinstances of {@link CyclopsBlock} will perform the inverse operation, being
 * that broken blocks will save the NBT data inside the dropped {@link BlockItem}.
 * @author rubensworks
 *
 */
public class ItemBlockNBT extends ItemBlockMetadata {
    
    /**
     * Make a new instance.
     * @param block The blockState instance.
     * @param builder Item properties builder.
     */
    public ItemBlockNBT(Block block, Item.Properties builder) {
        super(block, builder);
    }

    @Override
    protected boolean onBlockPlaced(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack itemStack, BlockState blockState) {
        if (super.onBlockPlaced(pos, world, player, itemStack, blockState)) {
            return true;
        }

        TileEntity tile = world.getTileEntity(pos);
        if (tile != null) {
            if (!world.isRemote() && tile.onlyOpsCanSetNbt() && (player == null || !player.canUseCommandBlock())) {
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
    protected boolean itemStackDataToTile(ItemStack itemStack, TileEntity tile) {
    	return false;
    }

}
