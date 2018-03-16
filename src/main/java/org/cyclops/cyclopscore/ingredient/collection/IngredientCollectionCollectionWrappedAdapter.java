package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Iterators;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;

import java.util.Collection;
import java.util.Iterator;

/**
 * An abstract collection that wraps instances in a {@link IngredientInstanceWrapper}
 * and stores those in a regular {@link Collection}.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <C> The type of collection that will contain the wrappers.
 */
public abstract class IngredientCollectionCollectionWrappedAdapter<T, M, C extends Collection<IngredientInstanceWrapper<T, M>>>
    extends IngredientCollectionAdapter<T, M> implements IIngredientCollectionMutable<T, M> {

    private final C collection;

    public IngredientCollectionCollectionWrappedAdapter(IngredientComponent<T, M> component, C collection) {
        super(component);
        this.collection = collection;
    }

    protected C getCollection() {
        return this.collection;
    }

    protected IngredientInstanceWrapper<T, M> wrap(T instance) {
        return getComponent().wrap(instance);
    }

    @Override
    public boolean add(T instance) {
        return getCollection().add(wrap(instance));
    }

    @Override
    public boolean remove(T instance) {
        return getCollection().remove(wrap(instance));
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
        return Iterators.transform(getCollection().iterator(), IngredientInstanceUnwrapperFunction.getInstance());
    }
}
