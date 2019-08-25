package org.cyclops.cyclopscore.datastructure;

import lombok.Data;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

/**
 * A simple data class for a block position inside a world.
 * @author rubensworks
 */
@Data(staticConstructor = "of")
public class DimPos implements Comparable<DimPos> {

    private final DimensionType dimension;
    private final BlockPos blockPos;
    private WeakReference<World> worldReference;

    private DimPos(DimensionType dimension, BlockPos blockPos, World world) {
        this.dimension = dimension;
        this.blockPos = blockPos;
        this.worldReference = world != null && world.isRemote() ? new WeakReference<>(world) : null;
    }

    private DimPos(DimensionType dimension, BlockPos blockPos) {
        this(dimension, blockPos, null);
    }

    public @Nullable World getWorld(boolean forceLoad) {
        if (worldReference == null) {
            return DimensionManager.getWorld(ServerLifecycleHooks.getCurrentServer(), dimension, false, forceLoad);
        }
        World world = worldReference.get();
        if (world == null) {
            world = DimensionManager.getWorld(ServerLifecycleHooks.getCurrentServer(), dimension, false, forceLoad);
            worldReference = new WeakReference<>(world);
        }
        return world;
    }

    public boolean isLoaded() {
        World world = getWorld(false);
        return world != null && world.isBlockLoaded(getBlockPos());
    }

    @Override
    public int compareTo(DimPos o) {
        int compareDim = Integer.compare(getDimension().getId(),
                o.getDimension().getId());
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
        return 31 * getDimension().getId() + getBlockPos().hashCode();
    }

    public static DimPos of(World world, BlockPos blockPos) {
        return new DimPos(world.getDimension().getType(), blockPos, world);
    }

    public static DimPos of(DimensionType dimension, BlockPos blockPos) {
        return new DimPos(dimension, blockPos);
    }

}
