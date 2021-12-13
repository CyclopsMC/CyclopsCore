package org.cyclops.cyclopscore.helper;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class contains helper methods involving directions.
 * 
 * @author immortaleeb
 *
 */
public class DirectionHelpers {
	/**
     * A list of all the {@link Direction}.
     */
    public static List<Direction> DIRECTIONS = Arrays.asList(Direction.values());
    /**
     * A double array that contains the visual side. The first argument should be the rotation of
     * the blockState and the second argument is the side for which the texture is called.
     */
    public static Direction[][] TEXTURESIDE_ORIENTATION = {
            {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}, // DOWN
            {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}, // UP
            {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}, // NORTH
            {Direction.DOWN, Direction.UP, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST}, // SOUTH
            {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH}, // WEST
            {Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH}, // EAST
    };
    private static Direction[][] FACING_ROTATIONS = {
            // DOWN, UP, NORTH, SOUTH, WEST, EAST
            {Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST}, // DOWN
            {Direction.SOUTH, Direction.NORTH, Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST}, // UP
            {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}, // NORTH
            {Direction.DOWN, Direction.UP, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST}, // SOUTH
            {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH}, // WEST
            {Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH}, // EAST
    };

    /**
     * Get an iterator for all the {@link Direction}.
     * @return The {@link Direction} iterator
     * @see DirectionHelpers#DIRECTIONS
     * @see Direction
     */
    public static Iterator<Direction> getDirectionIterator() {
        return DIRECTIONS.iterator();
    }

    /**
     * Get the EnumFacing the entity is facing, only vertical directions.
     * @param entity The entity that is facing a direction.
     * @return The {@link Direction} the entity is facing.
     */
    public static Direction getEntityFacingDirection(LivingEntity entity) {
        int facingDirection = MathHelper.floor((entity.yRot * 4F) / 360F + 0.5D) & 3;
        return Direction.from2DDataValue(facingDirection);
    }

    /**
     * Get the {@link Direction} from the sign of an X offset.
     * @param xSign X offset from somewhere.
     * @return The {@link Direction} for the offset.
     */
    public static Direction getEnumFacingFromXSign(int xSign) {
    	return xSign > 0 ? Direction.EAST : Direction.WEST;
    }

    /**
     * Get the {@link Direction} from the sign of an Z offset.
     * @param zSign Z offset from somewhere.
     * @return The {@link Direction} for the offset.
     */
    public static Direction getEnumFacingFromZSing(int zSign) {
    	return zSign > 0 ? Direction.SOUTH : Direction.NORTH;
    }

    /**
     * Transform the given facing based on a rotation.
     * The default rotation is {@link Direction#NORTH},
     * which means no transformation.
     * @param facing The facing to transform.
     * @param rotation The rotation.
     * @return The transformed facing.
     */
    public static Direction transformFacingForRotation(Direction facing, Direction rotation) {
        return FACING_ROTATIONS[rotation.ordinal()][facing.ordinal()];
    }
}
