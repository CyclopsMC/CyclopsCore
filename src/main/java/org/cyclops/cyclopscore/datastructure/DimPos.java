package org.cyclops.cyclopscore.datastructure;

import lombok.Data;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.lang.ref.WeakReference;

/**
 * A simple data class for a block position inside a world.
 * @author rubensworks
 */
@Data(staticConstructor = "of")
public class DimPos implements Comparable<DimPos> {

    private final int dimensionId;
    private final BlockPos blockPos;
    private WeakReference<World> worldReference;

    private DimPos(int dimensionId, BlockPos blockPos, World world) {
        this.dimensionId = dimensionId;
        this.blockPos = blockPos;
        this.worldReference = new WeakReference<>(world);
    }

    private DimPos(int dimensionId, BlockPos blockPos) {
        this(dimensionId, blockPos, null);
    }

    public World getWorld() {
        World world = worldReference.get();
        if (world == null) {
            world = net.minecraftforge.common.DimensionManager.getWorld(dimensionId);
            worldReference = new WeakReference<>(world);
        }
        return world;
    }

    @Override
    public int compareTo(DimPos o) {
        int compareDim = Integer.compare(getDimensionId(),
                o.getDimensionId());
        if(compareDim == 0) {
            return MinecraftHelpers.compareBlockPos(getBlockPos(), o.getBlockPos());
        }
        return compareDim;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DimPos && compareTo((DimPos) o) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * getDimensionId() + getBlockPos().hashCode();
    }

    public static DimPos of(World world, BlockPos blockPos) {
        return new DimPos(world.provider.getDimension(), blockPos, world);
    }

    public static DimPos of(int dimensionId, BlockPos blockPos) {
        return new DimPos(dimensionId, blockPos);
    }

}
