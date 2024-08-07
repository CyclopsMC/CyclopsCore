package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @author rubensworks
 */
public class BlockHelpersCommon implements IBlockHelpers {

    private static HolderGetter<Block> HOLDER_GETTER = new HolderGetter<>() {
        @Override
        public Optional<Holder.Reference<Block>> get(ResourceKey<Block> key) {
            return BuiltInRegistries.BLOCK.getHolder(key);
        }

        @Override
        public Optional<HolderSet.Named<Block>> get(TagKey<Block> p_256283_) {
            return Optional.empty();
        }
    };

    private final IModHelpers modHelpers;

    public BlockHelpersCommon(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public HolderGetter<Block> getHolderGetter() {
        return HOLDER_GETTER;
    }

    @Override
    public <T extends Comparable<T>> T getSafeBlockStateProperty(@Nullable BlockState state, Property<T> property, T fallback) {
        if(state == null) {
            return fallback;
        }
        T value;
        try {
            value = state.getValue(property);
        } catch (IllegalArgumentException e) {
            return fallback;
        }
        if(value == null) {
            return fallback;
        }
        return value;
    }

    @Override
    public CompoundTag serializeBlockState(BlockState blockState) {
        return NbtUtils.writeBlockState(blockState);
    }

    @Override
    public BlockState deserializeBlockState(HolderGetter<Block> holderGetter, CompoundTag serializedBlockState) {
        return NbtUtils.readBlockState(holderGetter, serializedBlockState);
    }

    @Override
    public BlockState getBlockStateFromItemStack(ItemStack itemStack) {
        Block block = ((BlockItem) itemStack.getItem()).getBlock();
        return block.defaultBlockState();
    }

    @Override
    public ItemStack getItemStackFromBlockState(BlockState blockState) {
        return new ItemStack(blockState.getBlock().asItem());
    }

    @Override
    public void markForUpdate(Level world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        world.sendBlockUpdated(pos, blockState, blockState, this.modHelpers.getMinecraftHelpers().getBlockNotify() | this.modHelpers.getMinecraftHelpers().getBlockNotifyClient());
    }

    @Override
    public void addCollisionBoxToList(BlockPos pos, AABB collidingBox, List<AABB> collisions, AABB addingBox) {
        if (addingBox != null) {
            AABB axisalignedbb = addingBox.move(pos);
            if (collidingBox.intersects(axisalignedbb)) {
                collisions.add(axisalignedbb);
            }
        }
    }

    @Override
    public boolean doesBlockHaveSolidTopSurface(LevelReader world, BlockPos blockPos) {
        return world.getBlockState(blockPos.offset(0, -1, 0)).isSolidRender(world, blockPos);
    }
}
