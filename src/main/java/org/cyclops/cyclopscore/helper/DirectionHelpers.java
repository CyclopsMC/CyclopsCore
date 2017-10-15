package org.cyclops.cyclopscore.helper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
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
     * A list of all the {@link net.minecraft.util.EnumFacing}.
     */
    public static List<EnumFacing> DIRECTIONS = Arrays.asList(EnumFacing.VALUES);
    /**
     * The facing directions of an entity, used in {@link DirectionHelpers#getEntityFacingDirection(net.minecraft.entity.EntityLivingBase)}.
     */
    public static final EnumFacing[] ENTITYFACING = EnumFacing.HORIZONTALS;
    /**
     * A double array that contains the visual side. The first argument should be the rotation of
     * the blockState and the second argument is the side for which the texture is called.
     */
    public static EnumFacing[][] TEXTURESIDE_ORIENTATION = {
            {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST}, // DOWN
            {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST}, // UP
            {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST}, // NORTH
            {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST}, // SOUTH
            {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH}, // WEST
            {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.NORTH}, // EAST
    };
    private static EnumFacing[][] FACING_ROTATIONS = {
            // DOWN, UP, NORTH, SOUTH, WEST, EAST
            {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST}, // DOWN
            {EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST}, // UP
            {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST}, // NORTH
            {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST}, // SOUTH
            {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH}, // WEST
            {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.NORTH}, // EAST
    };

    /**
     * Get an iterator for all the {@link net.minecraft.util.EnumFacing}.
     * @return The {@link net.minecraft.util.EnumFacing} iterator
     * @see DirectionHelpers#DIRECTIONS
     * @see net.minecraft.util.EnumFacing
     */
    public static Iterator<EnumFacing> getDirectionIterator() {
        return DIRECTIONS.iterator();
    }

    /**
     * Get the EnumFacing the entity is facing, only vertical directions.
     * @param entity The entity that is facing a direction.
     * @return The {@link net.minecraft.util.EnumFacing} the entity is facing.
     */
    public static EnumFacing getEntityFacingDirection(EntityLivingBase entity) {
        int facingDirection = MathHelper.floor((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;
        return ENTITYFACING[facingDirection];
    }

    /**
     * Get the {@link net.minecraft.util.EnumFacing} from the sign of an X offset.
     * @param xSign X offset from somewhere.
     * @return The {@link net.minecraft.util.EnumFacing} for the offset.
     */
    public static EnumFacing getEnumFacingFromXSign(int xSign) {
    	return xSign > 0 ? EnumFacing.EAST : EnumFacing.WEST;
    }

    /**
     * Get the {@link net.minecraft.util.EnumFacing} from the sign of an Z offset.
     * @param zSign Z offset from somewhere.
     * @return The {@link net.minecraft.util.EnumFacing} for the offset.
     */
    public static EnumFacing getEnumFacingFromZSing(int zSign) {
    	return zSign > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH;
    }

    /**
     * Transform the given facing based on a rotation.
     * The default rotation is {@link EnumFacing#NORTH},
     * which means no transformation.
     * @param facing The facing to transform.
     * @param rotation The rotation.
     * @return The transformed facing.
     */
    public static EnumFacing transformFacingForRotation(EnumFacing facing, EnumFacing rotation) {
        return FACING_ROTATIONS[rotation.ordinal()][facing.ordinal()];
    }
}
