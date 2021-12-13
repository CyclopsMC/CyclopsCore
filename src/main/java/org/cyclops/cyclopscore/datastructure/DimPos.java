package org.cyclops.cyclopscore.datastructure;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;
import lombok.SneakyThrows;
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
import java.util.concurrent.TimeUnit;

/**
 * A simple data class for a block position inside a world.
 * @author rubensworks
 */
@Data(staticConstructor = "of")
public class DimPos implements Comparable<DimPos> {

    private final String world;
    private final BlockPos blockPos;
    private WeakReference<World> worldReference;

    private static final LoadingCache<String, RegistryKey<World>> CACHE_WORLD_KEYS = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<String, RegistryKey<World>>() {
                @Override
                public RegistryKey<World> load(String key) {
                    return RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(key));
                }
            });

    private DimPos(String dimension, BlockPos blockPos, World world) {
        this.world = dimension;
        this.blockPos = blockPos;
        this.worldReference = world != null && world.isClientSide() ? new WeakReference<>(world) : null;
    }

    private DimPos(String world, BlockPos blockPos) {
        this(world, blockPos, null);
    }

    @SneakyThrows
    public RegistryKey<World> getWorldKey() {
        return CACHE_WORLD_KEYS.get(getWorld());
    }

    @Nullable
    public World getWorld(boolean forceLoad) {
        if (worldReference == null) {
            if (MinecraftHelpers.isClientSideThread()) {
                ClientWorld world = Minecraft.getInstance().level;
                if (world != null && world.dimension().location().toString().equals(this.getWorld())) {
                    this.worldReference = new WeakReference<>(world);
                    return this.worldReference.get();
                }
                return null;
            }
            return ServerLifecycleHooks.getCurrentServer().getLevel(getWorldKey());
        }

        World world = worldReference.get();
        if (world == null) {
            world = ServerLifecycleHooks.getCurrentServer().getLevel(getWorldKey());
            worldReference = new WeakReference<>(world);
        }
        return world;
    }

    public boolean isLoaded() {
        World world = getWorld(false);
        return world != null && world.hasChunkAt(getBlockPos());
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
        return of(world.dimension(), blockPos);
    }

    public static DimPos of(RegistryKey<World> world, BlockPos blockPos) {
        return new DimPos(world.location().toString(), blockPos);
    }

}
