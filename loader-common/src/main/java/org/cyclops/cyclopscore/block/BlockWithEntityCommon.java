package org.cyclops.cyclopscore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntityCommon;
import org.cyclops.cyclopscore.helper.IModHelpers;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Base block with a block entity.
 *
 * By default, the NBT data of block entities will not be persisted,
 * unless enabled via {@link #isPersistNbt()}.
 * If so, then the {@link #getDroppedItemStackNbt} method will be called
 * to call {@link CyclopsBlockEntityCommon#writeToItemStack(CompoundTag, HolderLookup.Provider)}.
 * This NBT data will automatically be read when placing the block.
 *
 * @author rubensworks
 */
public abstract class BlockWithEntityCommon extends BaseEntityBlock {

    private final BiFunction<BlockPos, BlockState, ? extends CyclopsBlockEntityCommon> blockEntitySupplier;

    public BlockWithEntityCommon(Properties properties, BiFunction<BlockPos, BlockState, ? extends CyclopsBlockEntityCommon> blockEntitySupplier) {
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
     * By default, {@link CyclopsBlockEntityCommon#writeToItemStack(CompoundTag, HolderLookup.Provider)} will be called.
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
                                                 CyclopsBlockEntityCommon blockEntity) {
        return blockEntity.writeToItemStack(new CompoundTag(), player.level().registryAccess());
    }

    /**
     * If the NBT data of this block entity should be added to the dropped item.
     * When overriding this, make sure to override the more sensitive getCloneItemStack methods in Forge and NeoForge,
     * and delegate to {@link #getCloneItemStack(BlockWithEntityCommon, Supplier, BlockState, HitResult, LevelReader, BlockPos, Player)}.
     * @return If the NBT data should be added.
     */
    public boolean isPersistNbt() {
        return false;
    }

    public static ItemStack getCloneItemStack(BlockWithEntityCommon self, Supplier<ItemStack> superMethod, BlockState state, HitResult target, LevelReader world,
                                              BlockPos blockPos, Player player) {
        ItemStack itemStack = self.getDroppedItemStack(state, target, world, blockPos, player, superMethod.get());
        if (self.isPersistNbt()) {
            IModHelpers.get().getBlockEntityHelpers().get(world, blockPos, CyclopsBlockEntityCommon.class).ifPresent(blockEntity -> {
                CompoundTag compoundnbt = self.getDroppedItemStackNbt(state, target, world, blockPos, player, itemStack, blockEntity);
                if (!compoundnbt.isEmpty()) {
                    itemStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundnbt));
                }
            });
        }
        return itemStack;
    }
}
