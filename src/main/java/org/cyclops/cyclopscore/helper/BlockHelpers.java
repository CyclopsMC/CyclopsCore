package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
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
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Contains helper methods for various block specific things.
 * @author rubensworks
 */
public final class BlockHelpers {

    public static HolderGetter<Block> HOLDER_GETTER_FORGE = new HolderGetter<Block>() {
        @Override
        public Optional<Holder.Reference<Block>> get(ResourceKey<Block> key) {
            return (Optional<Holder.Reference<Block>>) (Object) ForgeRegistries.BLOCKS.getHolder(key);
        }

        @Override
        public Optional<HolderSet.Named<Block>> get(TagKey<Block> p_256283_) {
            return Optional.empty();
        }
    };

    /**
     * Safely get a block state property for a nullable state and value that may not have been set yet.
     * @param state The block state.
     * @param property The property to get the value for.
     * @param fallback The fallback value when something has failed.
     * @param <T> The type of value to fetch.
     * @return The value.
     */
    public static <T extends Comparable<T>> T getSafeBlockStateProperty(@Nullable BlockState state, Property<T> property, T fallback) {
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

    /**
     * Convert the given blockstate to NBT.
     * @param blockState The blockstate to serialize.
     * @return The blockstate as NBT.
     */
    public static CompoundTag serializeBlockState(BlockState blockState) {
        return NbtUtils.writeBlockState(blockState);
    }

    /**
     * Convert the given serialized NBT blockstate to a blockstate instance.
     * @param holderGetter The block getter.
     * @param serializedBlockState The blockstate as NBT.
     * @return The resulting blockstate.
     */
    public static BlockState deserializeBlockState(HolderGetter<Block> holderGetter, CompoundTag serializedBlockState) {
        return NbtUtils.readBlockState(holderGetter, serializedBlockState);
    }

    /**
     * Get the blockstate from the given itemstack
     * @param itemStack The itemstack
     * @return The blockstate
     */
    public static BlockState getBlockStateFromItemStack(ItemStack itemStack) {
        Block block = ((BlockItem) itemStack.getItem()).getBlock();
        return block.defaultBlockState();
    }

    /**
     * Get the itemstack from the given blockstate
     * @param blockState The blockstate
     * @return The itemstack
     */
    public static ItemStack getItemStackFromBlockState(BlockState blockState) {
        return new ItemStack(blockState.getBlock().asItem());
    }

    /**
     * Trigger a block update.
     * @param world The world.
     * @param pos The pos.
     */
    public static void markForUpdate(Level world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        world.sendBlockUpdated(pos, blockState, blockState, MinecraftHelpers.BLOCK_NOTIFY | MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    }

    /**
     * Add a collision box to the given list if it intersects with a box.
     * @param pos The block position the collision is happening in.
     * @param collidingBox The box that is colliding with the block, absolute coordinates.
     * @param collisions The list fo add the box to.
     * @param addingBox The box to add to the lost, relative coordinates.
     */
    public static void addCollisionBoxToList(BlockPos pos, AABB collidingBox, List<AABB> collisions, AABB addingBox) {
        if (addingBox != null) {
            AABB axisalignedbb = addingBox.move(pos);
            if (collidingBox.intersects(axisalignedbb)) {
                collisions.add(axisalignedbb);
            }
        }
    }

    /**
     * If the given block has a solid top surface.
     * @param world The world`.
     * @param blockPos The block to check the top of.
     * @return If it has a solid top surface.
     */
    public static boolean doesBlockHaveSolidTopSurface(LevelReader world, BlockPos blockPos) {
        return world.getBlockState(blockPos.offset(0, -1, 0)).isSolidRender(world, blockPos);
    }

}
