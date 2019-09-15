package org.cyclops.cyclopscore.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * Base block with a tile entity.
 *
 * By default, the NBT data of tile entities will not be persisted,
 * unless enabled via {@link #isPersistNbt()}.
 * If so, then the {@link #getDroppedItemStackNbt} method will be called
 * to call {@link CyclopsTileEntity#writeToItemStack(CompoundNBT)}.
 * This NBT data will automatically be read when placing the block.
 *
 * @author rubensworks
 */
public class BlockTile extends ContainerBlock {

    private final Supplier<CyclopsTileEntity> tileEntitySupplier;

    public BlockTile(Properties properties, Supplier<CyclopsTileEntity> tileEntitySupplier) {
        super(properties);
        this.tileEntitySupplier = tileEntitySupplier;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return tileEntitySupplier.get();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world,
                                  BlockPos blockPos, PlayerEntity player) {
        ItemStack itemStack = this.getDroppedItemStack(state, target, world, blockPos, player,
                super.getPickBlock(state, target, world, blockPos, player));
        if (this.isPersistNbt()) {
            TileHelpers.getSafeTile(world, blockPos, CyclopsTileEntity.class).ifPresent(tile -> {
                CompoundNBT compoundnbt = getDroppedItemStackNbt(state, target, world, blockPos, player, itemStack, tile);
                if (!compoundnbt.isEmpty()) {
                    itemStack.setTagInfo("BlockEntityTag", compoundnbt);
                }
            });
        }
        return itemStack;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        TileEntity tileEntity = builder.get(LootParameters.BLOCK_ENTITY);
        if (tileEntity instanceof CyclopsTileEntity) {
            CyclopsTileEntity tile = (CyclopsTileEntity) tileEntity;
            // TODO: make something to add NBT to loot table stack. (take inspiration from shulkerbox) (do something like getPickBlock)
        }
        return super.getDrops(blockState, builder);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos blockPos, BlockState blockState, PlayerEntity player) {
        super.onBlockHarvested(world, blockPos, blockState, player);
    }

    /**
     * Override this method to modify the stack that is dropped.
     * @param state A block state.
     * @param target The ray trace result.
     * @param world The world.
     * @param blockPos The current position.
     * @param player The player breaking the block.
     * @param originalItemStack The original stack.
     * @return The modified stack.
     */
    protected ItemStack getDroppedItemStack(BlockState state, RayTraceResult target, IBlockReader world,
                                            BlockPos blockPos, PlayerEntity player, ItemStack originalItemStack) {
        return originalItemStack;
    }

    /**
     * Override this method to modify how NBT is constructed for the item.
     * By default, {@link CyclopsTileEntity#writeToItemStack(CompoundNBT)} will be called.
     * @param state A block state.
     * @param target The ray trace result.
     * @param world The world.
     * @param blockPos The current position.
     * @param player The player breaking the block.
     * @param itemStack The item stack.
     * @param tile The tile entity to serialize.
     * @return The NBT tag that will be added to the item stack.
     */
    protected CompoundNBT getDroppedItemStackNbt(BlockState state, RayTraceResult target, IBlockReader world,
                                                 BlockPos blockPos, PlayerEntity player, ItemStack itemStack,
                                                 CyclopsTileEntity tile) {
        return tile.writeToItemStack(new CompoundNBT());
    }

    /**
     * If the NBT data of this tile entity should be added to the dropped item.
     * @return If the NBT data should be added.
     */
    public boolean isPersistNbt() {
        return false;
    }
}
