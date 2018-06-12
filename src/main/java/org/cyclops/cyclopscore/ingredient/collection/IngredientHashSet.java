package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * An ingredient list collection that internally uses an {@link java.util.HashSet} to store instances.
 * This means that instances are hashed based on their component type's hash method.
 * @see java.util.HashSet
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientHashSet<T, M> extends IngredientSet<T, M> {

    public IngredientHashSet(IngredientComponent<T, M> component) {
        this(component, (Iterable<? extends T>) Sets.newHashSet());
    }

    public IngredientHashSet(IngredientComponent<T, M> component, int expectedSize) {
        super(component, Sets.newHashSetWithExpectedSize(expectedSize));
    }

    public IngredientHashSet(IngredientComponent<T, M> component, Iterable<? extends T> iterable) {
        super(component, iterable instanceof Collection ? Sets.newHashSetWithExpectedSize(((Collection) iterable).size()) : Sets.newHashSet());
        addAll(iterable);
    }

    public IngredientHashSet(IngredientComponent<T, M> component, Iterator<? extends T> iterable) {
        this(component);
        while (iterable.hasNext()) {
            add(iterable.next());
        }
    }

    public IngredientHashSet(IngredientComponent<T, M> component, HashSet<IngredientInstanceWrapper<T, M>> set) {
        super(component, set);
    }

}
