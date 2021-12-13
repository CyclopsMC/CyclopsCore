package org.cyclops.cyclopscore.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Helpers for world related logic.
 * @author rubensworks
 *
 */
public class WorldHelpers {
   
	/**
	 * The maximum chunk size for X and Z axis.
	 */
    public static final int CHUNK_SIZE = 16;
    
    private static final double TICK_LAG_REDUCTION_MODULUS_MODIFIER = 1.0D;

	/**
	 * Check if an efficient tick can happen.
	 * This is useful for opererations that should happen frequently, but not strictly every tick.
	 * @param world The world to tick in.
	 * @param baseModulus The amount of ticks that could be skipped.
	 * @param params Optional parameters to further vary the tick occurences.
	 * @return If a tick of some operation can occur.
	 */
	public static boolean efficientTick(World world, int baseModulus, int... params) {
		int mod = (int) (baseModulus * TICK_LAG_REDUCTION_MODULUS_MODIFIER);
		if(mod == 0) mod = 1;
		int offset = 0;
		for(int param : params) offset += param;
		return world.random.nextInt(mod) == Math.abs(offset) % mod;
	}

    /**
     * Check if an efficient tick can happen.
     * This is useful for opererations that should happen frequently, but not strictly every tick.
     * @param world The world to tick in.
     * @param baseModulus The amount of ticks that could be skipped.
     * @param blockPos The position to use as param.
     * @return If a tick of some operation can occur.
     */
    public static boolean efficientTick(World world, int baseModulus, BlockPos blockPos) {
        return efficientTick(world, baseModulus, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

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
    public static <T, W extends IWorld> T foldArea(W world, int[] areaMin, int[] areaMax, BlockPos blockPos, WorldFoldingFunction<T, T, W> folder, T value) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        for(int xc = x - areaMin[0]; xc <= x + areaMax[0]; xc++) {
            for(int yc = y - areaMin[1]; yc <= y + areaMax[1]; yc++) {
                for(int zc = z - areaMin[2]; zc <= z + areaMax[2]; zc++) {
                    value = folder.apply(value, world, new BlockPos(xc, yc, zc));
                }
            }
        }
        return value;
    }

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
    public static <T, W extends IWorld> T foldArea(W world, int area, BlockPos blockPos, WorldFoldingFunction<T, T, W> folder, T value) {
        return foldArea(world, new int[]{area, area, area}, new int[]{area, area, area}, blockPos, folder, value);
    }

    public static interface WorldFoldingFunction<F, T, W> {

        @Nullable
        public T apply(@Nullable F from, W world, BlockPos pos);

    }
    
}
