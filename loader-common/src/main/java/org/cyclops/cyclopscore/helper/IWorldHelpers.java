package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public interface IWorldHelpers {

    /**
     * The maximum chunk size for X and Z axis.
     */
    public int getChunkSize();

    /**
     * Check if an efficient tick can happen.
     * This is useful for opererations that should happen frequently, but not strictly every tick.
     * @param world The world to tick in.
     * @param baseModulus The amount of ticks that could be skipped.
     * @param params Optional parameters to further vary the tick occurences.
     * @return If a tick of some operation can occur.
     */
    public boolean efficientTick(Level world, int baseModulus, int... params);

    /**
     * Check if an efficient tick can happen.
     * This is useful for opererations that should happen frequently, but not strictly every tick.
     * @param world The world to tick in.
     * @param baseModulus The amount of ticks that could be skipped.
     * @param blockPos The position to use as param.
     * @return If a tick of some operation can occur.
     */
    public boolean efficientTick(Level world, int baseModulus, BlockPos blockPos);

    /**
     * Loop over a 3D area while accumulating a value.
     * @param world The world.
     * @param areaMin Radius array to start from {x, y, z}.
     * @param areaMax Radius array to end at (inclusive) {x, y, z}.
     * @param blockPos The position.
     * @param folder The folding function.
     * @param value The start value.
     * @param <T> The type of value to accumulate.
     * @param <W> The world type.
     * @return The resulting value.
     */
    public <T, W extends LevelAccessor> T foldArea(W world, int[] areaMin, int[] areaMax, BlockPos blockPos, WorldFoldingFunction<T, T, W> folder, T value);

    /**
     * Loop over a 3D area while accumulating a value.
     * @param world The world.
     * @param area Radius.
     * @param blockPos The position.
     * @param folder The folding function.
     * @param value The start value.
     * @param <T> The type of value to accumulate.
     * @param <W> The world type.
     * @return The resulting value.
     */
    public <T, W extends LevelAccessor> T foldArea(W world, int area, BlockPos blockPos, WorldFoldingFunction<T, T, W> folder, T value);

    /**
     * @return The current level client-side, or the overworld server-side.
     */
    public Level getActiveLevel();

    public static interface WorldFoldingFunction<F, T, W> {

        @Nullable
        public T apply(@Nullable F from, W world, BlockPos pos);

    }

}
