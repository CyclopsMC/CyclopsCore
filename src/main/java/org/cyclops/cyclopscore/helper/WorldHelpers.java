package org.cyclops.cyclopscore.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;

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
     * Set the biome for the given coordinates.
     * CAN ONLY BE CALLED ON SERVERS!
     * @param world The world.
     * @param pos The position.
     * @param biome The biome to change to.
     */
    //@SideOnly(Side.SERVER)
    @SuppressWarnings("unchecked")
    public static void setBiome(World world, BlockPos pos, BiomeGenBase biome) {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        if(chunk != null) {
        	BlockPos c = getChunkLocationFromWorldLocation(pos.getX(), 0, pos.getZ());
            int rx = c.getX();
            int rz = c.getZ();
            byte[] biomeArray = chunk.getBiomeArray();
            biomeArray[rz << 4 | rx] = (byte)(BiomeGenBase.getIdForBiome(biome) & 255);
            chunk.setChunkModified();
            world.getChunkProvider().provideChunk(chunk.xPosition, chunk.zPosition);
            world.markBlockRangeForRenderUpdate(pos, pos);
        } else {
            CyclopsCore.clog(Level.WARN, "Tried changing biome at non-existing chunk for position " + pos);
        }
    }

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
		return world.rand.nextInt(mod) == Math.abs(offset) % mod;
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
	 * Get the chunk location (coordinates within one chunk.) from a world location.
	 * @param x The world X.
	 * @param y The world Y.
	 * @param z The world Z.
	 * @return The chunk location.
	 */
	public static BlockPos getChunkLocationFromWorldLocation(int x, int y, int z) {
		return new BlockPos(x & (CHUNK_SIZE - 1), y, z & (CHUNK_SIZE - 1));
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
     * @return The resulting value.
     */
    public static <T> T foldArea(World world, int[] areaMin, int[] areaMax, BlockPos blockPos, WorldFoldingFunction<T, T> folder, T value) {
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
     * @return The resulting value.
     */
    public static <T> T foldArea(World world, int area, BlockPos blockPos, WorldFoldingFunction<T, T> folder, T value) {
        return foldArea(world, new int[]{area, area, area}, new int[]{area, area, area}, blockPos, folder, value);
    }

    public static interface WorldFoldingFunction<F, T> {

        @Nullable
        public T apply(@Nullable F from, World world, BlockPos pos);

    }
    
}
