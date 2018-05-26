package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Iterator;

/**
 * An ingredient collection that wraps over another ingredient collection.
 *
 * @param <T> An instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientCollectionWrapper<T, M, W extends IIngredientCollection<T, M>>
        implements IIngredientCollection<T, M> {

    private final W innerCollection;

    public IngredientCollectionWrapper(W innerCollection) {
        this.innerCollection = innerCollection;
    }

    protected W getInnerCollection() {
        return innerCollection;
    }

    @Override
    public boolean contains(T instance) {
        return this.getInnerCollection().contains(instance);
    }

    @Override
    public boolean contains(T instance, M matchCondition) {
        return this.getInnerCollection().contains(instance, matchCondition);
    }

    @Override
    public boolean containsAll(Iterable<? extends T> instances) {
        return this.getInnerCollection().containsAll(instances);
    }

    @Override
    public boolean containsAll(Iterable<? extends T> instances, M matchCondition) {
        return this.getInnerCollection().containsAll(instances, matchCondition);
    }

    @Override
    public int count(T instance, M matchCondition) {
        return this.getInnerCollection().count(instance, matchCondition);
    }

    @Override
    public IngredientComponent<T, M> getComponent() {
        return this.getInnerCollection().getComponent();
    }

    @Override
    public int size() {
        return this.getInnerCollection().size();
    }

    @Override
    public boolean isEmpty() {
        return this.getInnerCollection().isEmpty();
    }

    @Override
    public Iterator<T> iterator(T instance, M matchCondition) {
        return this.getInnerCollection().iterator(instance, matchCondition);
    }

    @Override
    public Iterator<T> iterator() {
        return this.getInnerCollection().iterator();
    }

    @Override
    public int hashCode() {
        return getInnerCollection().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return getInnerCollection().equals(obj);
    }

    @Override
    public String toString() {
        return getInnerCollection().toString();
    }
}
