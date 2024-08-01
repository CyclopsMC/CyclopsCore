package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Collection;
import java.util.Objects;

/**
 * A mutable collection of ingredient component instances.
 *
 * This does not conform to the {@link Collection} interface
 * as instances are compared using the matcher in {@link IngredientComponent#getMatcher()}.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public interface IIngredientCollectionMutable<T, M> extends IIngredientCollection<T, M> {

    /**
     * Add the given instance to the collection.
     * @param instance An instance.
     * @return If the collection was changed due to this addition.
     *         This can be false in the case of sets in which each instance can only exists once.
     */
    public boolean add(T instance);

    /**
     * Add the given instances to the collection.
     * @param instances Instances.
     * @return If the collection was changed due to this addition.
     *         This can be false in the case of sets in which each instance can only exists once.
     */
    default public boolean addAll(Iterable<? extends T> instances) {
        boolean changed = false;
        for (T instance : instances) {
            changed |= add(instance);
        }
        return changed;
    }

    /**
     * Remove the given instance from the collection.
     * @param instance An instance.
     * @return If the collection was changed due to this addition.
     *         Can be false if the collection did not contain the instance.
     */
    public boolean remove(T instance);

    /**
     * Remove all occurrences of the given instance under the given match conditions.
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
     * Remove all the given instances from the collection.
     * @param instances Instances.
     * @return The number of removed instances.
     */
    default public int removeAll(Iterable<? extends T> instances) {
        int removed = 0;
        for (T instance : instances) {
            if (remove(instance)) {
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
     * Clears this collection of all instances.
     */
    public void clear();

}
