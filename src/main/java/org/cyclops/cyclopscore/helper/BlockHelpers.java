package org.cyclops.cyclopscore.helper;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.state.IProperty;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Contains helper methods for various block specific things.
 * @author rubensworks
 */
public final class BlockHelpers {

    /**
     * Safely get a block state property for a nullable state and value that may not have been set yet.
     * @param state The block state.
     * @param property The property to get the value for.
     * @param fallback The fallback value when something has failed.
     * @param <T> The type of value to fetch.
     * @return The value.
     */
    public static <T extends Comparable<T>> T getSafeBlockStateProperty(@Nullable BlockState state, IProperty<T> property, T fallback) {
        if(state == null) {
            return fallback;
        }
        T value;
        try {
            value = state.get(property);
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
    public static INBT serializeBlockState(BlockState blockState) {
        return BlockState.serialize(NBTDynamicOps.INSTANCE, blockState).getValue();
    }

    /**
     * Convert the given serialized NBT blockstate to a blockstate instance.
     * @param serializedBlockState The blockstate as NBT.
     * @return The resulting blockstate. Can be null if the referred block does not exist.
     */
    public static BlockState deserializeBlockState(INBT serializedBlockState) {
        return BlockState.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, serializedBlockState));
    }

    /**
     * Get the blockstate from the given itemstack
     * @param itemStack The itemstack
     * @return The blockstate
     */
    public static BlockState getBlockStateFromItemStack(ItemStack itemStack) {
        Block block = ((BlockItem) itemStack.getItem()).getBlock();
        return block.getDefaultState();
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
    public static void markForUpdate(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, blockState, blockState, MinecraftHelpers.BLOCK_NOTIFY | MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    }

    /**
     * Add a collision box to the given list if it intersects with a box.
     * @param pos The block position the collision is happening in.
     * @param collidingBox The box that is colliding with the block, absolute coordinates.
     * @param collisions The list fo add the box to.
     * @param addingBox The box to add to the lost, relative coordinates.
     */
    public static void addCollisionBoxToList(BlockPos pos, AxisAlignedBB collidingBox, List<AxisAlignedBB> collisions, AxisAlignedBB addingBox) {
        if (addingBox != null) {
            AxisAlignedBB axisalignedbb = addingBox.offset(pos);
            if (collidingBox.intersects(axisalignedbb)) {
                collisions.add(axisalignedbb);
            }
        }
    }

    /**
     * If the given block has a solid top surface.
     * @param world The world.
     * @param blockPos The block to check the top of.
     * @return If it has a solid top surface.
     */
    public static boolean doesBlockHaveSolidTopSurface(IWorld world, BlockPos blockPos) {
        return world.getBlockState(blockPos.add(0, -1, 0)).isOpaqueCube(world, blockPos);
    }

}
