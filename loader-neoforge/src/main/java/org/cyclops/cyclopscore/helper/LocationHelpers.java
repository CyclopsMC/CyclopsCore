package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.cyclops.cyclopscore.network.PacketHandler;

import java.util.Random;

/**
 * Helper methods involving {@link BlockPos}S and {@link org.cyclops.cyclopscore.network.PacketHandler.TargetPoint}S.
 * @author immortaleeb
 *
 */
@Deprecated // TODO: remove in next major version
public class LocationHelpers {

    private static final Random random = new Random();

    /**
     * Creates a {@link PacketHandler.TargetPoint} for the dimension and position of the given {@link Entity}
     * and a given range.
     *
     * @param entity Entity who's dimension and position will be used to create the {@link PacketHandler.TargetPoint}.
     * @param range The range of the {@link PacketHandler.TargetPoint}.
     * @return A {@link PacketHandler.TargetPoint} with the position and dimension of the entity and the given range.
     */
    public static PacketHandler.TargetPoint createTargetPointFromEntity(Entity entity, int range) {
        return new PacketHandler.TargetPoint((ServerLevel) entity.level(), entity.getX(), entity.getY(), entity.getZ(), range, null);
    }

    /**
     * Creates a {@link PacketHandler.TargetPoint} for the dimension of the given world and the
     * given {@link BlockPos}.
     *
     * @param world The world from which the dimension will be used.
     * @param location The location for the target.
     * @param range The range of the {@link PacketHandler.TargetPoint}.
     * @return A {@link PacketHandler.TargetPoint} with the position and dimension of the entity and the given range.
     */
    public static PacketHandler.TargetPoint createTargetPointFromLocation(ServerLevel world, BlockPos location, int range) {
        return new PacketHandler.TargetPoint(world, location.getX(), location.getY(), location.getZ(), range, null);
    }

    /**
     * Get a random point inside a sphere in an efficient way.
     * @param center The center coordinates of the sphere.
     * @param radius The radius of the sphere.
     * @return The coordinates of the random point.
     */
    public static BlockPos getRandomPointInSphere(BlockPos center, int radius) {
        return IModHelpers.get().getLocationHelpers().getRandomPointInSphere(center, radius);
    }

    public static BlockPos copyLocation(BlockPos blockPos) {
        return IModHelpers.get().getLocationHelpers().copyLocation(blockPos);
    }

    public static Vec3i copyLocation(Vec3i blockPos) {
        return IModHelpers.get().getLocationHelpers().copyLocation(blockPos);
    }

    public static BlockPos addToDimension(BlockPos blockPos, int dimension, int value) {
        return IModHelpers.get().getLocationHelpers().addToDimension(blockPos, dimension, value);
    }

    public static BlockPos fromArray(int[] coordinates) {
        return IModHelpers.get().getLocationHelpers().fromArray(coordinates);
    }

    public static int[] toArray(Vec3i blockPos) {
        return IModHelpers.get().getLocationHelpers().toArray(blockPos);
    }

    public static BlockPos subtract(BlockPos blockPos, Vec3i vec) {
        return IModHelpers.get().getLocationHelpers().subtract(blockPos, vec);
    }

    public static Vec3i subtract(Vec3i vec1, Vec3i vec2) {
        return IModHelpers.get().getLocationHelpers().subtract(vec1, vec2);
    }

    /**
     * Get yaw from the start location to the end location.
     * @param start Start
     * @param end End
     * @return The yaw
     */
    public static double getYaw(BlockPos start, BlockPos end) {
        return IModHelpers.get().getLocationHelpers().getYaw(start, end);
    }

    /**
     * Get pitch from the start location to the end location.
     * @param start Start
     * @param end End
     * @return The pitch
     */
    public static double getPitch(BlockPos start, BlockPos end) {
        return IModHelpers.get().getLocationHelpers().getPitch(start, end);
    }

    /**
     * Compactly format a position.
     * @param pos The position.
     * @return The string.
     */
    public static String toCompactString(BlockPos pos) {
        return IModHelpers.get().getLocationHelpers().toCompactString(pos);
    }

    /**
     * Compactly format a vector.
     * @param vec The vector.
     * @return The string.
     */
    public static String toCompactString(Vec3i vec) {
        return IModHelpers.get().getLocationHelpers().toCompactString(vec);
    }

}
