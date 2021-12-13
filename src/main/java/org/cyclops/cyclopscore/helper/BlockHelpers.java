package org.cyclops.cyclopscore.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.Property;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
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
    public static CompoundNBT serializeBlockState(BlockState blockState) {
        return NBTUtil.writeBlockState(blockState);
    }

    /**
     * Convert the given serialized NBT blockstate to a blockstate instance.
     * @param serializedBlockState The blockstate as NBT.
     * @return The resulting blockstate.
     */
    public static BlockState deserializeBlockState(CompoundNBT serializedBlockState) {
        return NBTUtil.readBlockState(serializedBlockState);
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
    public static void markForUpdate(World world, BlockPos pos) {
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
    public static void addCollisionBoxToList(BlockPos pos, AxisAlignedBB collidingBox, List<AxisAlignedBB> collisions, AxisAlignedBB addingBox) {
        if (addingBox != null) {
            AxisAlignedBB axisalignedbb = addingBox.move(pos);
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
    public static boolean doesBlockHaveSolidTopSurface(IWorldReader world, BlockPos blockPos) {
        return world.getBlockState(blockPos.offset(0, -1, 0)).isSolidRender(world, blockPos);
    }

    /**
     * Set the fire info of the given block.
     * This is a copy of {@link FireBlock}'s setFireInfo.
     * @param blockIn The block.
     * @param encouragement The fire encouragement
     * @param flammability The flammability
     */
    public static void setFireInfo(Block blockIn, int encouragement, int flammability) {
        if (blockIn == Blocks.AIR) throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
        ((FireBlock) Blocks.FIRE).flameOdds.put(blockIn, encouragement);
        ((FireBlock) Blocks.FIRE).burnOdds.put(blockIn, flammability);
    }

}
