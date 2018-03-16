package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Iterator;

/**
 * Helper functions for ingredient collections.
 */
public final class IngredientCollections {

    IngredientCollections() {

    }

    /**
     * Create a new immutable empty collection.
     * @param ingredientComponent The ingredient component the collection should be made for.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return An immutable empty ingredient collection.
     */
    public static <T, M> IIngredientCollection<T, M> emptyCollection(IngredientComponent<T, M> ingredientComponent) {
        return new IngredientCollectionEmpty<>(ingredientComponent);
    }

    /**
     * Check if the two collection are equal by order.
     * @param c1 A first collection.
     * @param c2 A second collection.
     * @return If the two collection are equal by order.
     */
    public static boolean equalsOrdered(IIngredientCollection<?, ?> c1, IIngredientCollection<?, ?> c2) {
        return equalsCheckedOrdered((IIngredientCollection) c1, c2);
    }

    /**
     * Check if the two collection are equal by order using safe types.
     * @param c1 A first collection.
     * @param c2 A second collection.
     * @return If the two collection are equal by order.
     */
    public static <T, M> boolean equalsCheckedOrdered(IIngredientCollection<T, M> c1, IIngredientCollection<T, M> c2) {
        if (c1 == c2) {
            return true;
        }
        if (c1.getComponent() != c2.getComponent() || c1.size() != c2.size()) {
            return false;
        }
        Iterator<T> it1 = c1.iterator();
        Iterator<T> it2 = c2.iterator();
        IIngredientMatcher<T, M> matcher = c1.getComponent().getMatcher();
        while (it1.hasNext()) {
            if (!matcher.matchesExactly(it1.next(), it2.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Hash the given collection.
     *
     * This will hash each instance in the collection using the component type.
     *
     * @param collection A collection.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return A hashcode.
     */
    public static <T, M> int hash(IIngredientCollection<T, M> collection) {
        int hash = collection.getComponent().hashCode();
        IIngredientMatcher<T, M> matcher = collection.getComponent().getMatcher();
        for (T instance : collection) {
            hash = hash | matcher.hash(instance);
        }
        return hash;
    }

    /**
     * Stringifies the given collection.
     *
     * This will create a string by calling the toString() method on each instance in the collection.
     *
     * @param collection A collection.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return A string representation of the collection.
     */
    public static <T, M> String toString(IIngredientCollection<T, M> collection) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        boolean first = true;
        for (T instance : collection) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append(instance);
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
