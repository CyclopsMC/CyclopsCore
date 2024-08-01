package org.cyclops.cyclopscore.ingredient.collection;

/**
 * A mutable ingredient collection that wraps over another mutable ingredient collection.
 *
 * @param <T> An instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientCollectionMutableWrapper<T, M, W extends IIngredientCollectionMutable<T, M>>
        extends IngredientCollectionWrapper<T, M, W>
        implements IIngredientCollectionMutable<T, M> {

    public IngredientCollectionMutableWrapper(W innerCollection) {
        super(innerCollection);
    }

    @Override
    public boolean add(T instance) {
        return getInnerCollection().add(instance);
    }

    @Override
    public boolean addAll(Iterable<? extends T> instances) {
        return getInnerCollection().addAll(instances);
    }

    @Override
    public boolean remove(T instance) {
        return getInnerCollection().remove(instance);
    }

    @Override
    public int removeAll(T instance, M matchCondition) {
        return getInnerCollection().removeAll(instance, matchCondition);
    }

    @Override
    public int removeAll(Iterable<? extends T> instances) {
        return getInnerCollection().removeAll(instances);
    }

    @Override
    public int removeAll(Iterable<? extends T> instances, M matchCondition) {
        return getInnerCollection().removeAll(instances, matchCondition);
    }

    @Override
    public void clear() {
        getInnerCollection().clear();
    }
}
