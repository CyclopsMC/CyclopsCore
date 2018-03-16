package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Collection;

/**
 * An ingredient list collection that internally uses an {@link java.util.HashSet} to store instances.
 * This means that instances are hashed based on their component type's hash method.
 * @see java.util.HashSet
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientHashSet<T, M> extends IngredientSet<T, M> {

    public IngredientHashSet(IngredientComponent<T, M> component) {
        this(component, Sets.newHashSet());
    }

    public IngredientHashSet(IngredientComponent<T, M> component, int expectedSize) {
        super(component, Sets.newHashSetWithExpectedSize(expectedSize));
    }

    public IngredientHashSet(IngredientComponent<T, M> component, Iterable<? extends T> iterable) {
        super(component, iterable instanceof Collection ? Sets.newHashSetWithExpectedSize(((Collection) iterable).size()) : Sets.newHashSet());
        addAll(iterable);
    }

}
