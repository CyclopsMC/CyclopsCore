package org.cyclops.cyclopscore.helper;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.cyclopscore.datastructure.DimPos;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Contains helper methods for various tile entity specific things.
 * @author rubensworks
 */
public final class TileHelpers {

    /**
     * If tile entities should be retrieved in an unsafe manner from the non-main thread.
     * USE WITH CAUTION!!!
     */
    public static boolean UNSAFE_TILE_ENTITY_GETTER = false;

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
        TileEntity tile = UNSAFE_TILE_ENTITY_GETTER && world instanceof World && !((World) world).isClientSide() && Thread.currentThread() != ((World) world).thread ? getWorldBlockEntityUnchecked((World) world, pos) : world.getBlockEntity(pos);
        if (tile == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(targetClazz.cast(tile));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    /* WARNING: Hack to allow block entities to be retrieved from other threads. Needed for our IngredientObserver. */
    /* This is just a copy of {@link World#getBlockEntity} without the thread checks. */
    @Nullable
    static TileEntity getWorldBlockEntityUnchecked(World world, BlockPos pos) {
        if (World.isOutsideBuildHeight(pos)) {
            return null;
        } else {
            TileEntity tileentity = null;
            if (world.updatingBlockEntities) {
                tileentity = world.getPendingBlockEntityAt(pos);
            }
            if (tileentity == null) {
                // The following line causes another thread check in ServerChunkProvider#getChunk
                // So we override it inline
                tileentity = world.getChunkAt(pos).getBlockEntity(pos, Chunk.CreateEntityType.IMMEDIATE);
            }
            if (tileentity == null) {
                tileentity = world.getPendingBlockEntityAt(pos);
            }
            return tileentity;
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
