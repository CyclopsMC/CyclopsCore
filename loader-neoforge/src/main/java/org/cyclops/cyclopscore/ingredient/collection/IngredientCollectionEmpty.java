package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Collections;
import java.util.Iterator;

/**
 * An empty immutable ingredient collection.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientCollectionEmpty<T, M> implements IIngredientCollection<T, M> {

    private final IngredientComponent<T, M> component;

    public IngredientCollectionEmpty(IngredientComponent<T, M> component) {
        this.component = component;
    }

    @Override
    public IngredientComponent<T, M> getComponent() {
        return this.component;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean contains(T instance) {
        return false;
    }

    @Override
    public boolean contains(T instance, M matchCondition) {
        return false;
    }

    @Override
    public int count(T instance, M matchCondition) {
        return 0;
    }

    @Override
    public Iterator<T> iterator(T instance, M matchCondition) {
        return Collections.emptyIterator();
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof IIngredientCollection
                && IngredientCollections.equalsCheckedOrdered(this, (IIngredientCollection<T, M>) obj);
    }

    @Override
    public int hashCode() {
        return IngredientCollections.hash(this);
    }

    @Override
    public String toString() {
        return IngredientCollections.toString(this);
    }
}
