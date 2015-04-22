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
        int next = 0;
        if(counters.containsKey(key)) {
            next = counters.get(key);
        }
        counters.put(key, next + 1);
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
