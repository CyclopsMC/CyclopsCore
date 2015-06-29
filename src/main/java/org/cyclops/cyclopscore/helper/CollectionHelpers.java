package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper methods for collections.
 * @author rubensworks
 */
public final class CollectionHelpers {

    /**
     * Add the given key value pair in a map structure where multiple values for a key can exist.
     * This will take care of making a new list into the map when one did not exist before.
     * @param map The map.
     * @param key The key.
     * @param value The value.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static <K, V> void addToMapList(Map<K, List<V>> map, K key, V value) {
        List<V> collection = map.get(key);
        if(collection == null) {
            collection = Lists.newLinkedList();
            map.put(key, collection);
        }
        collection.add(value);
    }

    /**
     * Add the given key value pair in a map structure where multiple values for a key can exist.
     * This will take care of making a new set into the map when one did not exist before.
     * @param map The map.
     * @param key The key.
     * @param value The value.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static <K, V> void addToMapSet(Map<K, Set<V>> map, K key, V value) {
        Set<V> collection = map.get(key);
        if(collection == null) {
            collection = Sets.newHashSet();
            map.put(key, collection);
        }
        collection.add(value);
    }

}
