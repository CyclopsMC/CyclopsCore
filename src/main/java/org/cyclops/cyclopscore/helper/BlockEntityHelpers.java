package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.cyclops.cyclopscore.datastructure.DimPos;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Contains helper methods for various block entity specific things.
 * @author rubensworks
 */
public final class BlockEntityHelpers {

    /**
     * If block entities should be retrieved in an unsafe manner from the non-main thread.
     * USE WITH CAUTION!!!
     */
    public static boolean UNSAFE_BLOCK_ENTITY_GETTER = false;

    /**
     * Safely cast a block entity.
     * @param dimPos The dimensional position of the block providing the block entity.
     * @param targetClazz The class to cast to.
     * @param <T> The type of block entity to cast at.
     * @return The optional block entity.
     */
    public static <T> Optional<T> get(DimPos dimPos, Class<T> targetClazz) {
        Level level = dimPos.getLevel(true);
        if (level == null) {
            return Optional.empty();
        }
        return get(level, dimPos.getBlockPos(), targetClazz);
    }

    /**
     * Safely cast a block entity.
     * @param level The level.
     * @param pos The position of the block providing the block entity.
     * @param targetClazz The class to cast to.
     * @param <T> The type of block entity to cast at.
     * @return The optional block entity.
     */
    public static <T> Optional<T> get(BlockGetter level, BlockPos pos, Class<T> targetClazz) {
        BlockEntity blockEntity = UNSAFE_BLOCK_ENTITY_GETTER && level instanceof Level && !((Level) level).isClientSide() && Thread.currentThread() != ((Level) level).getServer().getRunningThread() ? getLevelBlockEntityUnchecked((Level) level, pos) : level.getBlockEntity(pos);
        if (blockEntity == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(targetClazz.cast(blockEntity));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    /* WARNING: Hack to allow block entities to be retrieved from other threads. Needed for our IngredientObserver. */
    /* This is just a copy of {@link Level#getBlockEntity} without the thread checks. */
    @Nullable
    static BlockEntity getLevelBlockEntityUnchecked(Level level, BlockPos pos) {
        if (level.isOutsideBuildHeight(pos)) {
            return null;
        } else {
            return level.getChunkAt(pos).getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE);
        }
    }

    /**
     * Safely get a capability from a block entity.
     * @param dimPos The dimensional position of the block providing the block entity.
     * @param capability The capability.
     * @param <T> The capability instance.
     * @param <C> The capability context.
     * @return The lazy optional capability.
     */
    public static <T, C> Optional<T> getCapability(DimPos dimPos, BlockCapability<T, C> capability) {
        Level level = dimPos.getLevel(true);
        if (level == null) {
            return Optional.empty();
        }
        return getCapability(level, dimPos.getBlockPos(), null, capability);
    }

    /**
     * Safely get a capability from a block entity.
     * @param dimPos The dimensional position of the block providing the block entity.
     * @param context The context to get the capability from.
     * @param capability The capability.
     * @param <T> The capability instance.
     * @param <C> The capability context.
     * @return The lazy optional capability.
     */
    public static <T, C> Optional<T> getCapability(DimPos dimPos, C context, BlockCapability<T, C> capability) {
        Level level = dimPos.getLevel(true);
        if (level == null) {
            return Optional.empty();
        }
        return getCapability(level, dimPos.getBlockPos(), context, capability);
    }

    /**
     * Safely get a capability from a block entity.
     * @param level The level.
     * @param pos The position of the block of the block entity providing the capability.
     * @param capability The capability.
     * @param <T> The capability instance.
     * @param <C> The capability context.
     * @return The lazy optional capability.
     */
    public static <T, C> Optional<T> getCapability(Level level, BlockPos pos, BlockCapability<T, C> capability) {
        return getCapability(level, pos, null, capability);
    }

    /**
     * Safely get a capability from a block entity.
     * @param level The level.
     * @param pos The position of the block of the block entity providing the capability.
     * @param context The context to get the capability from.
     * @param capability The capability.
     * @param <T> The capability instance.
     * @param <C> The capability context.
     * @return The lazy optional capability.
     */
    public static <T, C> Optional<T> getCapability(Level level, BlockPos pos, C context, BlockCapability<T, C> capability) {
        return Optional.ofNullable(level.getCapability(capability, pos, context));
    }

}
