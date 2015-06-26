package org.cyclops.cyclopscore.helper;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Contains helper methods for various tile entity specific things.
 * @author rubensworks
 */
public final class TileHelpers {

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

}
