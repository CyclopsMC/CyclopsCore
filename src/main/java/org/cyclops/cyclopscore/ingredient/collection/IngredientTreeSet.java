package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.LinkedList;

/**
 * An ingredient list collection that internally uses an {@link java.util.TreeSet} to store instances.
 * This means that instances are automatically ordered based on their component type's comparator.
 * @see java.util.TreeSet
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientTreeSet<T, M> extends IngredientSet<T, M> {

    public IngredientTreeSet(IngredientComponent<T, M> component) {
        super(component, Sets.newTreeSet());
    }

    public IngredientTreeSet(IngredientComponent<T, M> component, Iterable<? extends T> iterable) {
        super(component, Sets.newTreeSet());
        addAll(iterable);
    }

}
