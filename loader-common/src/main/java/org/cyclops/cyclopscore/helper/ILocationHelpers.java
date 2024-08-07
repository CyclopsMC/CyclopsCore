package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

/**
 * @author rubensworks
 */
public interface ILocationHelpers {

    /**
     * Get a random point inside a sphere in an efficient way.
     * @param center The center coordinates of the sphere.
     * @param radius The radius of the sphere.
     * @return The coordinates of the random point.
     */
    public BlockPos getRandomPointInSphere(BlockPos center, int radius);

    public BlockPos copyLocation(BlockPos blockPos);

    public Vec3i copyLocation(Vec3i blockPos);

    public BlockPos addToDimension(BlockPos blockPos, int dimension, int value);

    public BlockPos fromArray(int[] coordinates);

    public int[] toArray(Vec3i blockPos);

    public BlockPos subtract(BlockPos blockPos, Vec3i vec);

    public Vec3i subtract(Vec3i vec1, Vec3i vec2);

    public double getYaw(BlockPos start, BlockPos end);

    public double getPitch(BlockPos start, BlockPos end);

    public String toCompactString(BlockPos pos);

    public String toCompactString(Vec3i vec);

}
