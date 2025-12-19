package org.cyclops.cyclopscore.persist.world;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

import java.util.Map;

/**
 * Global counter that is shared over all dimensions, persisted, and consistent over server and clients.
 * @author rubensworks
 */
public class GlobalCounters extends WorldStorage<GlobalCounters> {

    private final Map<String, Integer> counters;

    public GlobalCounters(Map<String, Integer> counters) {
        this.counters = Maps.newHashMap(counters);
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
        setDirty();

        return next;
    }

    public static class Access extends WorldStorage.Access<GlobalCounters> {

        public Access(ModBaseNeoForge<?> mod) {
            super(new SavedDataType<>(
                    mod.getModId() + "_globalcounters",
                    (ctx) -> new GlobalCounters(Maps.newHashMap()),
                    ctx -> RecordCodecBuilder.create(instance -> instance.group(
                            RecordCodecBuilder.point(ctx.getLevel()),
                            Codec.dispatchedMap(Codec.STRING, (key) -> Codec.INT).fieldOf("counters").forGetter(data -> data.counters)
                    ).apply(instance, (level, counters) -> new GlobalCounters(counters)))
            ), mod);
        }
    }

}
