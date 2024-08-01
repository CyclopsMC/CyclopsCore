package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Iterator;

/**
 * A collection-like thing that contains ingredient component instances.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <I> The type that can be iterated over. This is typically just T.
 */
public interface IIngredientCollectionLike<T, M, I> extends Iterable<I> {

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
     * An iterator over this collection over all occurrences of the given instance under the given match conditions.
     * @param instance An instance.
     * @param matchCondition A match condition.
     * @return An iterator over this collection over all occurrences of the given instance
     *         under the given match conditions.
     */
    public Iterator<I> iterator(T instance, M matchCondition);

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
