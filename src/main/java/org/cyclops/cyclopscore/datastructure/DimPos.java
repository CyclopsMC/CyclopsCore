package org.cyclops.cyclopscore.datastructure;

import lombok.Data;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * A simple data class for a block position inside a world.
 * @author rubensworks
 */
@Data(staticConstructor = "of")
public class DimPos implements Comparable<DimPos> {

    private final World world;
    private final BlockPos blockPos;

    @Override
    public int compareTo(DimPos o) {
        int compareDim = Integer.compare(getWorld().provider.getDimensionId(),
                o.getWorld().provider.getDimensionId());
        if(compareDim == 0) {
            return MinecraftHelpers.compareBlockPos(getBlockPos(), o.getBlockPos());
        }
        return compareDim;
    }
}
