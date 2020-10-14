package org.cyclops.cyclopscore.helper;

import com.mojang.datafixers.util.Either;
import net.minecraft.profiler.IProfiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.cyclopscore.datastructure.DimPos;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
        TileEntity tile = UNSAFE_TILE_ENTITY_GETTER && world instanceof World && !((World) world).isRemote() ? getWorldTileEntityUnchecked((World) world, pos) : world.getTileEntity(pos);
        if (tile == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(targetClazz.cast(tile));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    /* WARNING: Hack to allow tile entities to be retrieved from other threads. Needed for our IngredientObserver. */
    /* This is just a copy of {@link World#getTileEntity} without the thread checks. */
    @Nullable
    static TileEntity getWorldTileEntityUnchecked(World world, BlockPos pos) {
        if (World.isOutsideBuildHeight(pos)) {
            return null;
        } else {
            TileEntity tileentity = null;
            if (world.processingLoadedTiles) {
                tileentity = world.getPendingTileEntityAt(pos);
            }
            if (tileentity == null) {
                // The following line causes another thread check in ServerChunkProvider#getChunk
                // So we override it inline
                //tileentity = world.getChunkAt(pos).getTileEntity(pos, Chunk.CreateEntityType.IMMEDIATE);
                tileentity = getChunkAtUnchecked(world, pos).getTileEntity(pos, Chunk.CreateEntityType.IMMEDIATE);
            }
            if (tileentity == null) {
                tileentity = world.getPendingTileEntityAt(pos);
            }
            return tileentity;
        }
    }

    static Chunk getChunkAtUnchecked(World world, BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        ChunkStatus requiredStatus = ChunkStatus.FULL;
        boolean load = true;

        ServerChunkProvider chunkProvider = (ServerChunkProvider) world.getChunkProvider();

        // The following is copied from ServerChunkProvider#getChunk, with the necessary AT additions

        IProfiler iprofiler = world.getProfiler();
        iprofiler.func_230035_c_("getChunk");
        long i = ChunkPos.asLong(chunkX, chunkZ);

        for(int j = 0; j < 4; ++j) {
            if (i == chunkProvider.recentPositions[j] && requiredStatus == chunkProvider.recentStatuses[j]) {
                IChunk ichunk = chunkProvider.recentChunks[j];
                if (ichunk != null || !load) {
                    return (Chunk) ichunk;
                }
            }
        }

        iprofiler.func_230035_c_("getChunkCacheMiss");
        CompletableFuture<Either<IChunk, ChunkHolder.IChunkLoadingError>> completablefuture = chunkProvider.func_217233_c(chunkX, chunkZ, requiredStatus, load);
        ((ThreadTaskExecutor<?>) chunkProvider.executor).driveUntil(completablefuture::isDone);
        IChunk ichunk1 = completablefuture.join().map((p_222874_0_) -> {
            return p_222874_0_;
        }, (p_222870_1_) -> {
            if (load) {
                throw (IllegalStateException) Util.pauseDevMode(new IllegalStateException("Chunk not there when requested: " + p_222870_1_));
            } else {
                return null;
            }
        });
        chunkProvider.func_225315_a(i, ichunk1, requiredStatus);
        return (Chunk) ichunk1;
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
