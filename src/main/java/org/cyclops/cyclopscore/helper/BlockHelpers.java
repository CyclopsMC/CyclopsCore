package org.cyclops.cyclopscore.helper;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

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
    public static <T> T getSafeBlockStateProperty(@Nullable IBlockState state, IProperty property, T fallback) {
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

}
