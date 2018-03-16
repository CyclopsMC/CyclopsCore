package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Iterator;

/**
 * An adapter class for mutable ingredient collections.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public abstract class IngredientCollectionAdapter<T, M> implements IIngredientCollectionMutable<T, M> {

    private final IngredientComponent<T, M> component;

    public IngredientCollectionAdapter(IngredientComponent<T, M> component) {
        this.component = component;
    }

    @Override
    public IngredientComponent<T, M> getComponent() {
        return this.component;
    }

    @Override
    public boolean remove(T instance) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            if (matcher.matchesExactly(instance, it.next())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public int removeAll(T instance, M matchCondition) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        Iterator<T> it = this.iterator();
        int count = 0;
        while (it.hasNext()) {
            if (matcher.matches(instance, it.next(), matchCondition)) {
                it.remove();
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean contains(T instance) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        for (T thisInstance : this) {
            if (matcher.matchesExactly(instance, thisInstance)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(T instance, M matchCondition) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        for (T thisInstance : this) {
            if (matcher.matches(instance, thisInstance, matchCondition)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int count(T instance, M matchCondition) {
        int count = 0;
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        for (T thisInstance : this) {
            if (matcher.matches(instance, thisInstance, matchCondition)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Iterator<T> iterator(T instance, M matchCondition) {
        return new FilteredIngredientIterator<>(this, getComponent().getMatcher(), instance, matchCondition);
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
