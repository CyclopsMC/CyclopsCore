package org.cyclops.cyclopscore.helper;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

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
    @SuppressWarnings("unchecked")
    public static <T> T getSafeBlockStateProperty(@Nullable IBlockState state, IProperty property, T fallback) {
        if(state == null) {
            return fallback;
        }
        Comparable value = state.getValue(property);
        if(value == null) {
            return fallback;
        }
        return (T) value;
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
        T value = state.getValue(property);
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
        String blockName = Block.blockRegistry.getNameForObject(blockState.getBlock()).toString();
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
        return new ItemStack(item, 1, blockState.getBlock().getMetaFromState(blockState));
    }

}
