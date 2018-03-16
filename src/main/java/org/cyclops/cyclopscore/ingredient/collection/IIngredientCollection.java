package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Iterator;
import java.util.Objects;

/**
 * A collection of ingredient component instances.
 *
 * This does not conform to the {@link java.util.Collection} interface
 * as instances are compared using the matcher in {@link IngredientComponent#getMatcher()}.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public interface IIngredientCollection<T, M> extends Iterable<T> {

    /**
     * @return The ingredient component type of which this collection contains instances.
     */
    public IngredientComponent<T, M> getComponent();

    /**
     * @return The number of instances contained in this collection.
     */
    public int size();

    /**
     * @return If the are no instances in this collection.
     */
    default public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Check if this collection contains the given instance.
     *
     * This instance will be compared based on the matcher from the ingredient component type.
     *
     * @param instance An instance.
     * @return If this collection contains the given instance.
     */
    public boolean contains(T instance);

    /**
     * Check if this collection contains the given instance under the given match conditions.
     *
     * This instance will be compared based on the matcher from the ingredient component type
     * using the given match condition.
     *
     * @param instance An instance.
     * @param matchCondition A match condition.
     * @return If this collection contains the given instance under the given match conditions.
     */
    public boolean contains(T instance, M matchCondition);

    /**
     * Check if this collection contains all the given instances.
     *
     * The instances will be compared based on the matcher from the ingredient component type.
     *
     * @param instances Instances.
     * @return If this collection contains all the given instances.
     */
    default public boolean containsAll(Iterable<? extends T> instances) {
        for (T instance : instances) {
            if (!contains(instance)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if this collection contains all the given instances under the given match conditions.
     *
     * The instances will be compared based on the matcher from the ingredient component type
     * using the given match condition.
     *
     * @param instances Instances.
     * @param matchCondition A match condition.
     * @return If this collection contains all the given instances under the given match conditions.
     */
    default public boolean containsAll(Iterable<? extends T> instances, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            return instances.iterator().hasNext() == !isEmpty();
        }
        for (T instance : instances) {
            if (!contains(instance, matchCondition)) {
                return false;
            }
        }
        return true;
    }

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
    public int count(T instance, M matchCondition);

    /**
     * An iterator over this collection over all occurrences of the given instance under the given match conditions.
     * @param instance An instance.
     * @param matchCondition A match condition.
     * @return An iterator over this collection over all occurrences of the given instance
     *         under the given match conditions.
     */
    public Iterator<T> iterator(T instance, M matchCondition);

    /**
     * Check if this collection equals the given object.
     * @param o An object.
     * @return True if the given object is an ingredient collection of the same type, component type
     *         and contains the same instances.
     */
    public boolean equals(Object o);

    /**
     * @return A hashcode of this collection, based on the hash codes, provided by the component type.
     */
    public int hashCode();

    /**
     * @return A string representation of this collection, provided by the component type.
     */
    public String toString();

}
