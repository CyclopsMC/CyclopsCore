package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Iterators;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * A ingredient collection using set semantics.
 * This means that each instances can only be present once in the collection based on its equals method.
 *
 * @see Set
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public abstract class IngredientSet<T, M>
        extends IngredientCollectionCollectionWrappedAdapter<T, M, Set<IngredientInstanceWrapper<T, M>>> {

    public IngredientSet(IngredientComponent<T, M> component, Set<IngredientInstanceWrapper<T, M>> set) {
        super(component, set);
    }

    @Override
    public int removeAll(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getExactMatchCondition(), matchCondition)) {
            return remove(instance) ? 1 : 0;
        }
        return super.removeAll(instance, matchCondition);
    }

    @Override
    public boolean contains(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getExactMatchCondition(), matchCondition)) {
            return contains(instance);
        }
        return super.contains(instance, matchCondition);
    }

    @Override
    public int count(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getExactMatchCondition(), matchCondition)) {
            return contains(instance) ? 1 : 0;
        }
        return super.count(instance, matchCondition);
    }

    @Override
    public Iterator<T> iterator(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getExactMatchCondition(), matchCondition)) {
            return contains(instance) ? new ExactIterator<>(instance, this) : Iterators.forArray();
        }
        return super.iterator(instance, matchCondition);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IngredientSet)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        IngredientSet<T, M> that = (IngredientSet<T, M>) obj;
        return this.getComponent() == that.getComponent() && this.size() == that.size() && this.containsAll(that);
    }

    protected static class ExactIterator<T> implements Iterator<T> {

        private final T instance;
        private final IIngredientCollectionMutable<T, ?> collection;
        private boolean nexted;
        private boolean removed;

        public ExactIterator(T instance, IIngredientCollectionMutable<T, ?> collection) {
            this.instance = instance;
            this.collection = collection;
        }

        @Override
        public boolean hasNext() {
            return !nexted;
        }

        @Override
        public T next() {
            if (nexted) {
                throw new NoSuchElementException("Tried reading a finished ExactIterator");
            }
            nexted = true;
            return instance;
        }

        @Override
        public void remove() {
            if (!nexted) {
                throw new IllegalStateException("The next method was not called yet");
            }
            if (removed) {
                throw new IllegalStateException("The remove method has already been called on this element");
            }
            removed = true;
            collection.remove(instance);
        }
    }
}
