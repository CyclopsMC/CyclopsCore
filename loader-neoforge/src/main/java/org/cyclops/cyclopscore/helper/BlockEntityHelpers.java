package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import org.cyclops.cyclopscore.datastructure.DimPos;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Contains helper methods for various block entity specific things.
 * @author rubensworks
 */
@Deprecated // TODO: remove in next major version
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
        return IModHelpersNeoForge.get().getBlockEntityHelpers().getLevelBlockEntityUnchecked(level, pos);
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
        return IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(dimPos, capability);
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
        return IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(dimPos, context, capability);
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
    public static <T, C> Optional<T> getCapability(ILevelExtension level, BlockPos pos, BlockCapability<T, C> capability) {
        return IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(level, pos, capability);
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
    public static <T, C> Optional<T> getCapability(ILevelExtension level, BlockPos pos, C context, BlockCapability<T, C> capability) {
        return IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(level, pos, context, capability);
    }

}
