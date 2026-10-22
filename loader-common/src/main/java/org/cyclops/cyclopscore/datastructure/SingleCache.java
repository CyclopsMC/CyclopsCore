package org.cyclops.cyclopscore.datastructure;

/**
 * A generic single object cache.
 * @param <K> The key type.
 * @param <V> The value type.
 * @author rubensworks
 */
public class SingleCache<K, V> {

    private boolean initialized = false;
    private K key = null;
    private V value = null;
    private ICacheUpdater<K, V> updater;

    public SingleCache(ICacheUpdater<K, V> updater) {
        this.updater = updater;
    }

    // Synchronized: packets are encoded on several Netty threads at once (e.g. one PacketCodec
    // instance broadcast to many players), and without it a thread could see initialized == true
    // while value was still null.
    public synchronized V get(K key) {
        if(!this.initialized || !this.updater.isKeyEqual(this.key, key)) {
            this.value = this.updater.getNewValue(key);
            this.key = key;
            this.initialized = true;
        }
        return this.value;
    }

    /**
     * This is responsible for fetching new updates when the cache desires this.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static interface ICacheUpdater<K, V> {

        public V getNewValue(K key);
        public boolean isKeyEqual(K cacheKey, K newKey);

    }

}
