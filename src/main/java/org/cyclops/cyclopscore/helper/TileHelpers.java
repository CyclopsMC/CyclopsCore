package org.cyclops.cyclopscore.helper;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.cyclopscore.datastructure.DimPos;

import java.util.Optional;

/**
 * Contains helper methods for various tile entity specific things.
 * @author rubensworks
 */
public final class TileHelpers {

    /**
     * Safely cast a tile entity.
     * @param dimPos The dimensional position of the block providing the tile entity.
     * @param targetClazz The class to cast to.
     * @param <T> The type of tile to cast at.
     * @return The optional tile entity.
     */
    public static <T> Optional<T> getSafeTile(DimPos dimPos, Class<T> targetClazz) {
        World world = dimPos.getWorld(true);
        if (world == null) {
            return Optional.empty();
        }
        return getSafeTile(world, dimPos.getBlockPos(), targetClazz);
    }

    /**
     * Safely cast a tile entity.
     * @param world The world.
     * @param pos The position of the block providing the tile entity.
     * @param targetClazz The class to cast to.
     * @param <T> The type of tile to cast at.
     * @return The optional tile entity.
     */
    public static <T> Optional<T> getSafeTile(IBlockReader world, BlockPos pos, Class<T> targetClazz) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(targetClazz.cast(tile));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    /**
     * Safely get a capability from a tile.
     * @param dimPos The dimensional position of the block providing the tile entity.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The lazy optional capability.
     */
    public static <C> LazyOptional<C> getCapability(DimPos dimPos, Capability<C> capability) {
        World world = dimPos.getWorld(true);
        if (world == null) {
            return LazyOptional.empty();
        }
        return getCapability(world, dimPos.getBlockPos(), null, capability);
    }

    /**
     * Safely get a capability from a tile.
     * @param dimPos The dimensional position of the block providing the tile entity.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The lazy optional capability.
     */
    public static <C> LazyOptional<C> getCapability(DimPos dimPos, Direction side, Capability<C> capability) {
        World world = dimPos.getWorld(true);
        if (world == null) {
            return LazyOptional.empty();
        }
        return getCapability(world, dimPos.getBlockPos(), side, capability);
    }

    /**
     * Safely get a capability from a tile.
     * @param world The world.
     * @param pos The position of the block of the tile entity providing the capability.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The lazy optional capability.
     */
    public static <C> LazyOptional<C> getCapability(IBlockReader world, BlockPos pos, Capability<C> capability) {
        return getCapability(world, pos, null, capability);
    }

    /**
     * Safely get a capability from a tile.
     * @param world The world.
     * @param pos The position of the block of the tile entity providing the capability.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The lazy optional capability.
     */
    public static <C> LazyOptional<C> getCapability(IBlockReader world, BlockPos pos, Direction side, Capability<C> capability) {
        TileEntity tile = TileHelpers.getSafeTile(world, pos, TileEntity.class).orElse(null);
        if(tile != null) {
            return tile.getCapability(capability, side);
        }
        return LazyOptional.empty();
    }

}
