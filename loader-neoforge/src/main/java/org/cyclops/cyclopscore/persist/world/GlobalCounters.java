package org.cyclops.cyclopscore.persist.world;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

import java.util.Map;

/**
 * Global counter that is shared over all dimensions, persisted, and consistent over server and clients.
 * @author rubensworks
 */
public class GlobalCounters extends WorldStorage<GlobalCounters> {

    private final ServerLevel level;
    private final Map<String, Integer> counters;

    public GlobalCounters(ModBaseNeoForge mod, SavedData.Context ctx) {
        super(mod);
        this.level = ctx.level();
        counters = Maps.newHashMap();
    }

    public GlobalCounters(ModBaseNeoForge mod, ServerLevel level, Map<String, Integer> counters) {
        super(mod);
        this.level = level;
        this.counters = counters;
    }

    /**
     * Get the next counter value for the given key.
     * @param key the key for the counter.
     * @return The next counter value.
     */
    public synchronized int getNext(String key) {
        // Get value from counter map
        Integer nextObject = counters.get(key);
        int next = 0;
        if (nextObject != null) {
            next = nextObject;
        }

        // Handle overflows
        int incr = next + 1;
        if (incr < 0) {
            incr = 0;
        }

        // Store value for next call
        counters.put(key, incr);

        return next;
    }

    @Override
    public void reset() {
        counters.clear();
    }

    @Override
    protected SavedDataType<GlobalCounters> constructSavedDataType() {
        return new SavedDataType<>(
                this.mod.getModId() + "_globalcounters",
                (ctx) -> new GlobalCounters(this.mod, ctx),
                ctx -> RecordCodecBuilder.create(instance -> instance.group(
                        RecordCodecBuilder.point(ctx.levelOrThrow()),
                        Codec.dispatchedMap(Codec.STRING, (key) -> Codec.INT).fieldOf("counters").forGetter(data -> data.counters)
                ).apply(instance, (level, counters) -> new GlobalCounters(this.mod, level, counters)))
        );
    }

}
