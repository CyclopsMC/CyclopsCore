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
     * @param <T> The type of tile to cast at.
     * @return The tile entity or null.
     */
    public static <T extends TileEntity> T getSafeTile(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile != null) {
            try {
                return (T) world.getTileEntity(pos);
            } catch (ClassCastException e) {}
        }
        return null;
    }

}
