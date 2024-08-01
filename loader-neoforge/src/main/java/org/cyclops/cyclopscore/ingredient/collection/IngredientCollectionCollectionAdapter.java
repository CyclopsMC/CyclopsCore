package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Collection;
import java.util.Iterator;

/**
 * An abstract collection that stores instances directly in a regular {@link Collection}.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <C> The type of inner collection.
 */
public abstract class IngredientCollectionCollectionAdapter<T, M, C extends Collection<T>>
        extends IngredientCollectionAdapter<T, M> implements IIngredientCollectionMutable<T, M> {

    private final C collection;

    public IngredientCollectionCollectionAdapter(IngredientComponent<T, M> component, C collection) {
        super(component);
        this.collection = collection;
    }

    protected C getCollection() {
        return this.collection;
    }

    @Override
    public boolean add(T instance) {
        return getCollection().add(instance);
    }

    @Override
    public void clear() {
        getCollection().clear();
    }

    @Override
    public int size() {
        return getCollection().size();
    }

    @Override
    public Iterator<T> iterator() {
        return getCollection().iterator();
    }
}
