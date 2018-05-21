package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

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
     * Check if the two maps are equal.
     * @param c1 A first map.
     * @param c2 A second map.
     * @return If the two maps are equal.
     */
    public static boolean equalsMap(IIngredientMap<?, ?, ?> c1, IIngredientMap<?, ?, ?> c2) {
        if (c1 == c2) {
            return true;
        }
        if (c1.getComponent() != c2.getComponent()) {
            return false;
        }
        return equalsMapChecked((IIngredientMap) c1, c2);
    }

    /**
     * Check if the two maps are equal using safe types.
     * @param c1 A first map.
     * @param c2 A second map.
     * @return If the two maps are equal.
     */
    public static <T, M, V> boolean equalsMapChecked(IIngredientMap<T, M, V> c1, IIngredientMap<T, M, V> c2) {
        if (c1.size() != c2.size()) {
            return false;
        }
        for (Map.Entry<T, V> entry : c1) {
            if (!Objects.equals(c2.get(entry.getKey()), entry.getValue())) {
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
     * Hash the given map.
     *
     * This will hash each instance in the map using the component type.
     *
     * @param map A map.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @param <V> The type of mapped values.
     * @return A hashcode.
     */
    public static <T, M, V> int hash(IIngredientMap<T, M, V> map) {
        int hash = map.getComponent().hashCode();
        IIngredientMatcher<T, M> matcher = map.getComponent().getMatcher();
        for (Map.Entry<T, V> entry : map.entrySet()) {
            hash = hash | (matcher.hash(entry.getKey()) ^ entry.getValue().hashCode());
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

    /**
     * Stringifies the given map.
     *
     * This will create a string by calling the toString() method on each entry in the map.
     *
     * @param map A map.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @param <V> The type of mapped values.
     * @return A string representation of the map.
     */
    public static <T, M, V> String toString(IIngredientMap<T, M, V> map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        boolean first = true;
        for (Map.Entry<T, V> entry : map) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append("{");
            stringBuilder.append(entry.getKey().toString());
            stringBuilder.append(",");
            stringBuilder.append(entry.getValue().toString());
            stringBuilder.append("}");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
