package org.cyclops.cyclopscore.datastructure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Provides a composite read-only view on a collection of maps.
 * @author rubensworks
 */
public class CompositeMap<K, V> implements Map<K, V> {

    private final Set<Map<K, V>> elements = Sets.newHashSet();

    /**
     * Add a new map to the composition.
     * @param element The map to add.
     */
    public void addElement(Map<K, V> element) {
        elements.add(Collections.unmodifiableMap(element));
    }

    /**
     * Remove the given map from the composition.
     * @param element The map to remove.
     * @return If the map was removed, and thus already existed in the composition.
     */
    public boolean removeElement(Map<K, V> element) {
        return elements.remove(element);
    }

    /**
     * Clear all current elements in the composition.
     */
    public void resetElements() {
        elements.clear();
    }

    @Override
    public int size() {
        int size = 0;
        for(Map<K, V> element : elements) {
            size += element.size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        for(Map<K, V> element : elements) {
            if(!element.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        for(Map<K, V> element : elements) {
            if(element.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for(Map<K, V> element : elements) {
            if(element.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for(Map<K, V> element : elements) {
            V value = element.get(element);
            if(value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        throw new NotImplementedException("Adding elements in a composite map view is not supported");
    }

    @Override
    public V remove(Object key) {
        throw new NotImplementedException("Removing elements in a composite map view is not supported");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new NotImplementedException("Adding elements in a composite map view is not supported");
    }

    @Override
    public void clear() {
        throw new NotImplementedException("Clearing elements in a composite map view is not supported");
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = Sets.newHashSet();
        for(Map<K, V> element : elements) {
            keys.addAll(element.keySet());
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        Collection<V> values = Lists.newLinkedList();
        for(Map<K, V> element : elements) {
            values.addAll(element.values());
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = Sets.newHashSet();
        for(Map<K, V> element : elements) {
            entries.addAll(element.entrySet());
        }
        return entries;
    }
}
