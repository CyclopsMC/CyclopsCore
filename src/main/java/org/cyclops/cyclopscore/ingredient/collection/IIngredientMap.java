package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A mapping from ingredient component instances to values of any type.
 *
 * This does not conform to the {@link java.util.Map} interface
 * as instances are compared using the matcher in {@link IngredientComponent#getMatcher()}.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <V> The type of mapped values.
 */
public interface IIngredientMap<T, M, V> extends IIngredientCollectionLike<T, M, Map.Entry<T, V>> {

    /**
     * Check if this map contains the given instance.
     *
     * This instance will be compared based on the matcher from the ingredient component type.
     *
     * @param instance An instance.
     * @return If this map contains the given instance.
     */
    public default boolean containsKey(T instance) {
        return get(instance) != null;
    }

    /**
     * Check if this map contains the given instance under the given match conditions.
     *
     * This instance will be compared based on the matcher from the ingredient component type
     * using the given match condition.
     *
     * @param instance An instance.
     * @param matchCondition A match condition.
     * @return If this map contains the given instance under the given match conditions.
     */
    public default boolean containsKey(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            return !isEmpty();
        }
        return !keySet(instance, matchCondition).isEmpty();
    }

    /**
     * Check if this map contains all the given instances.
     *
     * The instances will be compared based on the matcher from the ingredient component type.
     *
     * @param instances Instances.
     * @return If this map contains all the given instances.
     */
    default public boolean containsKeyAll(Iterable<? extends T> instances) {
        for (T instance : instances) {
            if (!containsKey(instance)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if this map contains all the given instances under the given match conditions.
     *
     * The instances will be compared based on the matcher from the ingredient component type
     * using the given match condition.
     *
     * @param instances Instances.
     * @param matchCondition A match condition.
     * @return If this map contains all the given instances under the given match conditions.
     */
    default public boolean containsKeyAll(Iterable<? extends T> instances, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            return instances.iterator().hasNext() == !isEmpty();
        }
        for (T instance : instances) {
            if (!containsKey(instance, matchCondition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if this map contains the given value.
     * @param value A value.
     * @return If this map contains the given value.
     */
    public boolean containsValue(V value);

    /**
     * Count the number of occurrences of the given instance under the given match conditions.
     *
     * This instance will be compared based on the matcher from the ingredient component type
     * using the given match condition.
     *
     * @param instance An instance.
     * @param matchCondition A match condition.
     * @return The number of occurrences of the given instance under the given match conditions.
     */
    public default int countKey(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            return size();
        }
        return keySet(instance, matchCondition).size();
    }

    /**
     * Get the value for the given instance.
     * @param key An instance.
     * @return The value.
     */
    @Nullable
    public V get(T key);

    /**
     * Get all values for all instances that match with the given instance under the given match conditions.
     *
     * This instance will be compared based on the matcher from the ingredient component type
     * using the given match condition.
     *
     * @param key An instance.
     * @param matchCondition A match condition.
     * @return All matching values.
     */
    public Collection<V> getAll(T key, M matchCondition);

    /**
     * @return All key instances.
     */
    IngredientSet<T, M> keySet();

    /**
     * Get all key instances that match the given instance.
     *
     * This instance will be compared based on the matcher from the ingredient component type
     * using the given match condition.
     *
     * @param key An instance.
     * @param matchCondition A match condition.
     * @return All key instances that match the given instance.
     */
    IngredientSet<T, M> keySet(T key, M matchCondition);

    /**
     * @return All values.
     */
    Collection<V> values();

    /**
     * @return All instance-value entries in this map.
     */
    Set<Map.Entry<T, V>> entrySet();

}
