package org.cyclops.cyclopscore.persist.world;

import com.google.common.collect.Maps;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;

import java.util.Map;

/**
 * Global counter that is shared over all dimensions, persisted, and consistent over server and clients.
 * @author rubensworks
 */
public class GlobalCounters extends WorldStorage {

    @NBTPersist
    private Map<String, Integer> counters = Maps.newHashMap();

    public GlobalCounters(ModBase mod) {
        super(mod);
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
    protected String getDataId() {
        return "GlobalCounterData";
    }

}
