package org.cyclops.cyclopscore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;

import java.util.function.BiFunction;

/**
 * Base block with a block entity.
 *
 * By default, the NBT data of block entities will not be persisted,
 * unless enabled via {@link #isPersistNbt()}.
 * If so, then the {@link #getDroppedItemStackNbt} method will be called
 * to call {@link CyclopsBlockEntity#writeToItemStack(CompoundTag, HolderLookup.Provider)}.
 * This NBT data will automatically be read when placing the block.
 *
 * @author rubensworks
 */
@Deprecated // TODO: Use BlockWithEntityCommon instead; rm in next major
public abstract class BlockWithEntity extends BlockWithEntityCommon {
    public BlockWithEntity(Properties properties, BiFunction<BlockPos, BlockState, CyclopsBlockEntity> blockEntitySupplier) {
        super(properties, blockEntitySupplier);
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
                    itemStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundnbt));
                }
            });
        }
        return itemStack;
    }
}
