package org.cyclops.cyclopscore.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.block.BlockWithEntity;

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

    // BlockItem#updateCustomBlockEntityTag became static in MC 26.3, so we hook into block placement instead.
    // Vanilla block entity data still takes precedence, as it is applied after this.
    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState placementState) {
        if (!super.placeBlock(context, placementState)) {
            return false;
        }

        Level world = context.getLevel();
        if (!world.isClientSide()) {
            BlockEntity tile = world.getBlockEntity(context.getClickedPos());
            if (tile != null) {
                itemStackDataToTile(context.getItemInHand().copy().split(1), tile);
            }
        }

        return true;
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
