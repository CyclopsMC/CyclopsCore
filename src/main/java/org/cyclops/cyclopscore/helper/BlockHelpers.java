package org.cyclops.cyclopscore.helper;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.commoncapabilities.api.capability.block.BlockCapabilities;
import org.cyclops.cyclopscore.datastructure.DimPos;

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
    public static <T extends Comparable<T>> T getSafeBlockStateProperty(@Nullable IBlockState state, IProperty<T> property, T fallback) {
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
     * Safely get an extended block state property for a nullable state and value that may not have been set yet.
     * @param state The block state.
     * @param property The property to get the value for.
     * @param fallback The fallback value when something has failed.
     * @param <T> The type of value to fetch.
     * @return The value.
     */
    public static <T> T getSafeBlockStateProperty(@Nullable IExtendedBlockState state, IUnlistedProperty<T> property,
                                                  T fallback) {
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
     * Convert the given blockstate to a pair of blockname string and meta value.
     * @param blockState The blockstate to serialize.
     * @return The pair of the blockname and meta value.
     */
    public static Pair<String, Integer> serializeBlockState(IBlockState blockState) {
        String blockName = Block.REGISTRY.getNameForObject(blockState.getBlock()).toString();
        int meta = blockState.getBlock().getMetaFromState(blockState);
        return Pair.of(blockName, meta);
    }

    /**
     * Convert the given serialized blockstate to a blockstate instance.
     * @param serializedBlockState The pair of the blockname and meta value.
     * @return The resulting blockstate. Can be null if the referred block does not exist.
     */
    public static IBlockState deserializeBlockState(Pair<String, Integer> serializedBlockState) {
        Block block = Block.getBlockFromName(serializedBlockState.getLeft());
        if(block != null) {
            return block.getStateFromMeta(serializedBlockState.getRight());
        }
        return null;
    }

    /**
     * Get the blockstate from the given itemstack
     * @param itemStack The itemstack
     * @return The blockstate
     */
    public static IBlockState getBlockStateFromItemStack(ItemStack itemStack) {
        Block block = ((ItemBlock) itemStack.getItem()).getBlock();
        return block.getStateFromMeta(itemStack.getMetadata());
    }

    /**
     * Get the itemstack from the given blockstate
     * @param blockState The blockstate
     * @return The itemstack
     */
    public static ItemStack getItemStackFromBlockState(IBlockState blockState) {
        Item item = Item.getItemFromBlock(blockState.getBlock());
        if(item == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(item, 1, item.getHasSubtypes() ? blockState.getBlock().damageDropped(blockState) : 0);
    }

    /**
     * Trigger a block update.
     * @param world The world.
     * @param pos The pos.
     */
    public static void markForUpdate(World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
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
    public static boolean doesBlockHaveSolidTopSurface(IBlockAccess world, BlockPos blockPos) {
        return world.getBlockState(blockPos.add(0, -1, 0)).isOpaqueCube();
    }

    /**
     * If the given block can be displayed in the given creative tab.
     * @param block The block.
     * @param creativeTab The creative tab.
     * @return If it can be displayed.
     */
    public static boolean isValidCreativeTab(Block block, @Nullable CreativeTabs creativeTab) {
        return creativeTab == null
                || creativeTab == CreativeTabs.SEARCH
                || block.getCreativeTabToDisplayOn() == creativeTab;
    }

    /**
     * Safely get a capability from a block.
     * @param dimPos The dimensional position of the block.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The capability or null.
     */
    @Optional.Method(modid = "commoncapabilities")
    public static <C> C getCapability(DimPos dimPos, Capability<C> capability) {
        World world = dimPos.getWorld();
        if (world == null) {
            return null;
        }
        return getCapability(world, dimPos.getBlockPos(), null, capability);
    }

    /**
     * Safely get a capability from a block.
     * @param dimPos The dimensional position of the block.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The capability or null.
     */
    @Optional.Method(modid = "commoncapabilities")
    public static <C> C getCapability(DimPos dimPos, EnumFacing side, Capability<C> capability) {
        World world = dimPos.getWorld();
        if (world == null) {
            return null;
        }
        return getCapability(world, dimPos.getBlockPos(), side, capability);
    }

    /**
     * Safely get a capability from a block.
     * @param world The world.
     * @param pos The position of the block providing the capability.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The capability or null.
     */
    @Optional.Method(modid = "commoncapabilities")
    public static <C> C getCapability(World world, BlockPos pos, EnumFacing side, Capability<C> capability) {
        return getCapability((IBlockAccess) world, pos, side, capability);
    }

    /**
     * Safely get a capability from a block.
     * @param world The world.
     * @param pos The position of the block providing the capability.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The capability or null.
     */
    @Optional.Method(modid = "commoncapabilities")
    public static <C> C getCapability(IBlockAccess world, BlockPos pos, Capability<C> capability) {
        return getCapability(world, pos, null, capability);
    }

    /**
     * Safely get a capability from a block.
     * @param world The world.
     * @param pos The position of the block providing the capability.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The capability or null.
     */
    @Optional.Method(modid = "commoncapabilities")
    public static <C> C getCapability(IBlockAccess world, BlockPos pos, EnumFacing side, Capability<C> capability) {
        IBlockState blockState = world.getBlockState(pos);
        if(BlockCapabilities.getInstance().hasCapability(blockState, capability, world, pos, side)) {
            return BlockCapabilities.getInstance().getCapability(blockState, capability, world, pos, side);
        }
        return null;
    }

}
