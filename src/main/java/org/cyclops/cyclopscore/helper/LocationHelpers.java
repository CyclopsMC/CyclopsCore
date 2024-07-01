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
        BlockPos randomPoint = null;
        while(randomPoint == null) {
            BlockPos coordinates = center.offset(- radius + random.nextInt(2 * radius),
                    - radius + random.nextInt(2 * radius), - radius + random.nextInt(2 * radius));
            double totalDistance = center.distSqr(coordinates);
            if((int) Math.sqrt(totalDistance) <= radius) {
                randomPoint = coordinates;
            }
        }
        return randomPoint;
    }

    public static BlockPos copyLocation(BlockPos blockPos) {
        return new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static Vec3i copyLocation(Vec3i blockPos) {
        return new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static BlockPos addToDimension(BlockPos blockPos, int dimension, int value) {
        if(dimension == 0) return blockPos.offset(value, 0, 0);
        if(dimension == 1) return blockPos.offset(0, value, 0);
        if(dimension == 2) return blockPos.offset(0, 0, value);
        return blockPos;
    }

    public static BlockPos fromArray(int[] coordinates) {
        return new BlockPos(coordinates[0], coordinates[1], coordinates[2]);
    }

    public static int[] toArray(Vec3i blockPos) {
        return new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
    }

    public static BlockPos subtract(BlockPos blockPos, Vec3i vec) {
        return new BlockPos(blockPos.getX() - vec.getX(), blockPos.getY() - vec.getY(), blockPos.getZ() - vec.getZ());
    }

    public static Vec3i subtract(Vec3i vec1, Vec3i vec2) {
        return new Vec3i(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY(), vec1.getZ() - vec2.getZ());
    }

    /**
     * Get yaw from the start location to the end location.
     * @param start Start
     * @param end End
     * @return The yaw
     */
    public static double getYaw(BlockPos start, BlockPos end) {
        double dX = start.getX() - end.getX();
        double dY = start.getY() - end.getY();
        double dZ = start.getZ() - end.getZ();
        return Math.atan2(dZ, dX) * 180 / Math.PI;
    }

    /**
     * Get pitch from the start location to the end location.
     * @param start Start
     * @param end End
     * @return The pitch
     */
    public static double getPitch(BlockPos start, BlockPos end) {
        double dX = start.getX() - end.getX();
        double dY = start.getY() - end.getY();
        double dZ = start.getZ() - end.getZ();
        return (Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI) * 180 / Math.PI;
    }

    /**
     * Compactly format a position.
     * @param pos The position.
     * @return The string.
     */
    public static String toCompactString(BlockPos pos) {
        return String.format("x: %s ; y: %s ;z: %s", pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Compactly format a vector.
     * @param vec The vector.
     * @return The string.
     */
    public static String toCompactString(Vec3i vec) {
        return String.format("%sx%sx%s", vec.getX(), vec.getY(), vec.getZ());
    }

}
