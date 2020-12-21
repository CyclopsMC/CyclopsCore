package org.cyclops.cyclopscore.block.multi;

import net.minecraft.util.math.vector.Vector3i;
import org.cyclops.cyclopscore.helper.LocationHelpers;

/**
 * Helpers for size validators.
 * @author rubensworks
 */
public class SizeValidators {

    /**
     * Comparator for {@link Vector3i}.
     * @param vec1 First vector.
     * @param vec2 Second vector.
     * @return The comparison result.
     */
    public static int compareVec3i(Vector3i vec1, Vector3i vec2) {
        int i = 0;
        boolean validBuffer = false;
        int buffer = Integer.MAX_VALUE; // This is used to store the minimum compared value > 0
        int[] v1 = LocationHelpers.toArray(vec1);
        int[] v2 = LocationHelpers.toArray(vec2);
        while(i < 3) {
            if(v1[i] != v2[i]) {
                int comp =  v1[i] - v2[i];
                if(comp < 0) {
                    return comp;
                } else {
                    validBuffer = true;
                    buffer = Math.min(buffer, comp);
                }
            }
            i++;
        }
        return validBuffer ? buffer: 0;
    }

}
