package org.cyclops.cyclopscore.datastructure;

import lombok.Data;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import javax.annotation.Nullable;
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
        this.worldReference = world != null && world.isRemote ? new WeakReference<>(world) : null;
    }

    private DimPos(int dimensionId, BlockPos blockPos) {
        this(dimensionId, blockPos, null);
    }

    public @Nullable World getWorld() {
        if (worldReference == null) {
            return net.minecraftforge.common.DimensionManager.getWorld(dimensionId);
        }
        World world = worldReference.get();
        if (world == null) {
            world = net.minecraftforge.common.DimensionManager.getWorld(dimensionId);
            worldReference = new WeakReference<>(world);
        }
        return world;
    }

    public boolean isLoaded() {
        World world = getWorld();
        return world != null && world.isBlockLoaded(getBlockPos());
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
