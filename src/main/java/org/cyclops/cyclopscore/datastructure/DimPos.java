package org.cyclops.cyclopscore.datastructure;

import lombok.Data;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple data class for a block position inside a world.
 * @author rubensworks
 */
@Data(staticConstructor = "of")
public class DimPos implements Comparable<DimPos> {

    private final String world;
    private final BlockPos blockPos;
    private WeakReference<World> worldReference;

    // RegistryKey.UNIVERSAL_KEY_MAP is private and can not be shrunken
    // so it is probably safe to assume that worldKeyCache should never be flushed
    private static final Map<String, RegistryKey<World>> worldKeyCache = new HashMap<>();

    private DimPos(String dimension, BlockPos blockPos, World world) {
        this.world = dimension;
        this.blockPos = blockPos;
        this.worldReference = world != null && world.isRemote() ? new WeakReference<>(world) : null;
    }

    private DimPos(String world, BlockPos blockPos) {
        this(world, blockPos, null);
    }

    public RegistryKey<World> getWorldKey() {
        final RegistryKey<World> getKey = worldKeyCache.get(world);

        if (getKey != null) {
            return getKey;
        }

        final RegistryKey<World> newKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(world));
        worldKeyCache.put(world, newKey);

        return newKey;
    }

    @Nullable
    public World getWorld(boolean forceLoad) {
        if (worldReference == null) {
            if (MinecraftHelpers.isClientSideThread()) {
                final ClientWorld world = Minecraft.getInstance().world;

                if (world.getDimensionKey().getLocation().toString().equals(this.getWorld())) {
                    this.worldReference = new WeakReference<>(world);
                    return this.worldReference.get();
                }

                return null;
            }

            return ServerLifecycleHooks.getCurrentServer().getWorld(getWorldKey());
        }

        World world = worldReference.get();

        if (world == null) {
            world = ServerLifecycleHooks.getCurrentServer().getWorld(getWorldKey());
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
        int compareDim = getWorld().compareTo(o.getWorld());
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
        return 31 * getWorld().hashCode() + getBlockPos().hashCode();
    }

    public static DimPos of(World world, BlockPos blockPos) {
        return of(world.getDimensionKey(), blockPos);
    }

    public static DimPos of(RegistryKey<World> world, BlockPos blockPos) {
        return new DimPos(world.getLocation().toString(), blockPos);
    }

}
