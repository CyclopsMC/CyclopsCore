package org.cyclops.cyclopscore.helper;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import org.cyclops.cyclopscore.datastructure.DimPos;

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
     * @return The tile entity or null.
     */
    public static <T> T getSafeTile(DimPos dimPos, Class<T> targetClazz) {
        return getSafeTile(dimPos.getWorld(), dimPos.getBlockPos(), targetClazz);
    }

    /**
     * Safely cast a tile entity.
     * @param world The world.
     * @param pos The position of the block providing the tile entity.
     * @param targetClazz The class to cast to.
     * @param <T> The type of tile to cast at.
     * @return The tile entity or null.
     */
    public static <T> T getSafeTile(IBlockAccess world, BlockPos pos, Class<T> targetClazz) {
        TileEntity tile = world.getTileEntity(pos);
        try {
            return targetClazz.cast(tile);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Safely get a capability from a tile.
     * @param dimPos The dimensional position of the block providing the tile entity.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The capability or null.
     */
    public static <C> C getCapability(DimPos dimPos, EnumFacing side, Capability<C> capability) {
        return getCapability(dimPos.getWorld(), dimPos.getBlockPos(), side, capability);
    }

    /**
     * Safely get a capability from a tile.
     * @param world The world.
     * @param pos The position of the block of the tile entity providing the capability.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The capability or null.
     */
    public static <C> C getCapability(IBlockAccess world, BlockPos pos, EnumFacing side, Capability<C> capability) {
        TileEntity tile = TileHelpers.getSafeTile(world, pos, TileEntity.class);
        if(tile != null && tile.hasCapability(capability, side)) {
            return tile.getCapability(capability, side);
        }
        return null;
    }

}
