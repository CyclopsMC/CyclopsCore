package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

import java.util.Random;

/**
 * @author rubensworks
 */
public class LocationHelpersCommon implements ILocationHelpers {
    private static final Random random = new Random();

    @Override
    public BlockPos getRandomPointInSphere(BlockPos center, int radius) {
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

    @Override
    public BlockPos copyLocation(BlockPos blockPos) {
        return new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public Vec3i copyLocation(Vec3i blockPos) {
        return new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public BlockPos addToDimension(BlockPos blockPos, int dimension, int value) {
        if(dimension == 0) return blockPos.offset(value, 0, 0);
        if(dimension == 1) return blockPos.offset(0, value, 0);
        if(dimension == 2) return blockPos.offset(0, 0, value);
        return blockPos;
    }

    @Override
    public BlockPos fromArray(int[] coordinates) {
        return new BlockPos(coordinates[0], coordinates[1], coordinates[2]);
    }

    @Override
    public int[] toArray(Vec3i blockPos) {
        return new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
    }

    @Override
    public BlockPos subtract(BlockPos blockPos, Vec3i vec) {
        return new BlockPos(blockPos.getX() - vec.getX(), blockPos.getY() - vec.getY(), blockPos.getZ() - vec.getZ());
    }

    @Override
    public Vec3i subtract(Vec3i vec1, Vec3i vec2) {
        return new Vec3i(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY(), vec1.getZ() - vec2.getZ());
    }

    @Override
    public double getYaw(BlockPos start, BlockPos end) {
        double dX = start.getX() - end.getX();
        double dY = start.getY() - end.getY();
        double dZ = start.getZ() - end.getZ();
        return Math.atan2(dZ, dX) * 180 / Math.PI;
    }

    @Override
    public double getPitch(BlockPos start, BlockPos end) {
        double dX = start.getX() - end.getX();
        double dY = start.getY() - end.getY();
        double dZ = start.getZ() - end.getZ();
        return (Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI) * 180 / Math.PI;
    }

    @Override
    public String toCompactString(BlockPos pos) {
        return String.format("x: %s ; y: %s ;z: %s", pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public String toCompactString(Vec3i vec) {
        return String.format("%sx%sx%s", vec.getX(), vec.getY(), vec.getZ());
    }
}
