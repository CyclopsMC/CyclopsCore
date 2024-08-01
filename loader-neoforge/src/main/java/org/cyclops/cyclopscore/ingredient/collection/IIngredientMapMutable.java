package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

/**
 * A mutable mapping from ingredient component instances to values of any type.
 *
 * This does not conform to the {@link java.util.Map} interface
 * as instances are compared using the matcher in {@link IngredientComponent#getMatcher()}.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <V> The type of mapped values.
 */
public interface IIngredientMapMutable<T, M, V> extends IIngredientMap<T, M, V> {

    /**
     * Clears this map of all instances.
     */
    public void clear();

    /**
     * Add a new entry.
     * @param key An instance key.
     * @param value A value.
     * @return The previous value that was associated with the given instance.
     */
    @Nullable
    V put(T key, V value);

    /**
     * Remove the mapping for the given key instance.
     * @param key An instance key.
     * @return The value that was associated with the given instance.
     */
    @Nullable
    V remove(T key);

    /**
     * Remove all occurrences of the given key instance under the given match conditions.
     *
     * This instance will be compared based on the matcher from the ingredient component type
     * using the given match condition.
     *
     * @param instance An instance.
     * @param matchCondition A match condition.
     * @return The number of removed instances.
     */
    public int removeAll(T instance, M matchCondition);

    /**
     * Remove all the given key instances from the collection.
     * @param instances Instances.
     * @return The number of removed instances.
     */
    default public int removeAll(Iterable<? extends T> instances) {
        int removed = 0;
        for (T instance : instances) {
            if (remove(instance) != null) {
                removed++;
            }
        }
        return removed;
    }

    /**
     * Remove all occurrences of the given instances under the given match conditions.
     *
     * The instances will be compared based on the matcher from the ingredient component type
     * using the given match condition.
     *
     * @param instances Instances.
     * @param matchCondition A match condition.
     * @return The number of removed instances.
     */
    default public int removeAll(Iterable<? extends T> instances, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            int size = size();
            this.clear();
            return size;
        }
        int removed = 0;
        for (T instance : instances) {
            removed += removeAll(instance, matchCondition);
        }
        return removed;
    }

    /**
     * Add all entries from the given map to this map.
     * @param map A map, that will not be changed, only read.
     * @return The amount of entries from the given map that were added to this map.
     */
    default int putAll(IIngredientMap<? extends T, M, ? extends V> map) {
        for (Map.Entry<? extends T, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
        return map.size();
    }


}
