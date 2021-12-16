package org.cyclops.cyclopscore.datastructure;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;
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

    private final String level;
    private final BlockPos blockPos;
    private WeakReference<Level> worldReference;

    private static final LoadingCache<String, ResourceKey<Level>> CACHE_WORLD_KEYS = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<String, ResourceKey<Level>>() {
                @Override
                public ResourceKey<Level> load(String key) {
                    return ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(key));
                }
            });

    private DimPos(String dimension, BlockPos blockPos, Level world) {
        this.level = dimension;
        this.blockPos = blockPos;
        this.worldReference = world != null && world.isClientSide() ? new WeakReference<>(world) : null;
    }

    private DimPos(String world, BlockPos blockPos) {
        this(world, blockPos, null);
    }

    @SneakyThrows
    public ResourceKey<Level> getLevelKey() {
        return CACHE_WORLD_KEYS.get(getLevel());
    }

    @Nullable
    public Level getLevel(boolean forceLoad) {
        if (worldReference == null) {
            if (MinecraftHelpers.isClientSideThread()) {
                ClientLevel world = Minecraft.getInstance().level;
                if (world != null && world.dimension().location().toString().equals(this.getLevel())) {
                    this.worldReference = new WeakReference<>(world);
                    return this.worldReference.get();
                }
                return null;
            }
            return ServerLifecycleHooks.getCurrentServer().getLevel(getLevelKey());
        }

        Level world = worldReference.get();
        if (world == null) {
            world = ServerLifecycleHooks.getCurrentServer().getLevel(getLevelKey());
            worldReference = new WeakReference<>(world);
        }
        return world;
    }

    public boolean isLoaded() {
        Level world = getLevel(false);
        return world != null && world.hasChunkAt(getBlockPos());
    }

    @Override
    public int compareTo(DimPos o) {
        int compareDim = getLevel().compareTo(o.getLevel());
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
        return 31 * getLevel().hashCode() + getBlockPos().hashCode();
    }

    public static DimPos of(Level world, BlockPos blockPos) {
        return of(world.dimension(), blockPos);
    }

    public static DimPos of(ResourceKey<Level> world, BlockPos blockPos) {
        return new DimPos(world.location().toString(), blockPos);
    }

}
