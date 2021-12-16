package org.cyclops.cyclopscore.helper;

import net.minecraft.core.Direction;

/**
 * Contains helper methods for matrix transformations.
 * Contains several methods from BuildCraft
 * @author rubensworks
 */
public final class MatrixHelpers {

    /**
     * Mirror the given box bounds around the Y axis
     * @param targetArray The box bounds to transform.
     */
    public static void mirrorY(float[][] targetArray) {
        float temp = targetArray[1][0];
        targetArray[1][0] = (targetArray[1][1] - 0.5F) * -1F + 0.5F; // 1 -> 0.5F -> -0.5F -> 0F
        targetArray[1][1] = (temp - 0.5F) * -1F + 0.5F; // 0 -> -0.5F -> 0.5F -> 1F
    }

    /**
     * Rotate the given box bounds once.
     * Shifts the x to y, y to z and z to x.
     * @param targetArray The box bounds to transform.
     */
    public static void rotate(float[][] targetArray) {
        for (int i = 0; i < 2; i++) {
            float temp = targetArray[2][i];
            targetArray[2][i] = targetArray[1][i];
            targetArray[1][i] = targetArray[0][i];
            targetArray[0][i] = temp;
        }
    }

    /**
     * Transform the given box bounds to the given orientation.
     * @param targetArray The box bounds to transform.
     * @param direction The orientation to transform to.
     */
    public static void transform(float[][] targetArray, Direction direction) {
        if((direction.ordinal() & 0x1) == 1) {
            mirrorY(targetArray);
        }
        for(int i = 0; i < (direction.ordinal() >> 1); i++) {
            rotate(targetArray);
        }
    }

}
