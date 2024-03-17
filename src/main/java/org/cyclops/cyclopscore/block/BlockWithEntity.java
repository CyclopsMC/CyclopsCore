package org.cyclops.cyclopscore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

/**
 * Base block with a block entity.
 *
 * By default, the NBT data of block entities will not be persisted,
 * unless enabled via {@link #isPersistNbt()}.
 * If so, then the {@link #getDroppedItemStackNbt} method will be called
 * to call {@link CyclopsBlockEntity#writeToItemStack(CompoundTag)}.
 * This NBT data will automatically be read when placing the block.
 *
 * @author rubensworks
 */
public abstract class BlockWithEntity extends BaseEntityBlock {

    private final BiFunction<BlockPos, BlockState, CyclopsBlockEntity> blockEntitySupplier;

    public BlockWithEntity(Properties properties, BiFunction<BlockPos, BlockState, CyclopsBlockEntity> blockEntitySupplier) {
        super(properties);
        this.blockEntitySupplier = blockEntitySupplier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return blockEntitySupplier.apply(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader world,
                                  BlockPos blockPos, Player player) {
        ItemStack itemStack = this.getDroppedItemStack(state, target, world, blockPos, player,
                super.getCloneItemStack(state, target, world, blockPos, player));
        if (this.isPersistNbt()) {
            BlockEntityHelpers.get(world, blockPos, CyclopsBlockEntity.class).ifPresent(blockEntity -> {
                CompoundTag compoundnbt = getDroppedItemStackNbt(state, target, world, blockPos, player, itemStack, blockEntity);
                if (!compoundnbt.isEmpty()) {
                    itemStack.addTagElement("BlockEntityTag", compoundnbt);
                }
            });
        }
        return itemStack;
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos blockPos, BlockState blockState, Player player) {
        return super.playerWillDestroy(world, blockPos, blockState, player);
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
    protected ItemStack getDroppedItemStack(BlockState state, HitResult target, BlockGetter world,
                                            BlockPos blockPos, Player player, ItemStack originalItemStack) {
        return originalItemStack;
    }

    /**
     * Override this method to modify how NBT is constructed for the item.
     * By default, {@link CyclopsBlockEntity#writeToItemStack(CompoundTag)} will be called.
     * @param state A block state.
     * @param target The ray trace result.
     * @param world The world.
     * @param blockPos The current position.
     * @param player The player breaking the block.
     * @param itemStack The item stack.
     * @param blockEntity The block entity to serialize.
     * @return The NBT tag that will be added to the item stack.
     */
    protected CompoundTag getDroppedItemStackNbt(BlockState state, HitResult target, BlockGetter world,
                                                 BlockPos blockPos, Player player, ItemStack itemStack,
                                                 CyclopsBlockEntity blockEntity) {
        return blockEntity.writeToItemStack(new CompoundTag());
    }

    /**
     * If the NBT data of this block entity should be added to the dropped item.
     * @return If the NBT data should be added.
     */
    public boolean isPersistNbt() {
        return false;
    }
}
