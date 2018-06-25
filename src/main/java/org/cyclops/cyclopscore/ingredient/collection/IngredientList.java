package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.ListIterator;

/**
 * A mutable ingredient collection using list semantics.
 * This means that instances exist in a predefined order and that instances can exist multiple time in the collection.
 *
 * @see List
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientList<T, M> extends IngredientCollectionCollectionAdapter<T, M, List<T>>
        implements IIngredientList<T, M> {

    public IngredientList(IngredientComponent<T, M> component, List<T> list) {
        super(component, list);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof IngredientList
                && IngredientCollections.equalsCheckedOrdered(this, (IIngredientCollection<T, M>) obj);
    }

    @Nullable
    @Override
    public T get(int index) {
        return getCollection().get(index);
    }

    @Nullable
    @Override
    public T set(int index, T instance) {
        return getCollection().set(index, instance);
    }

    @Override
    public void add(int index, T instance) {
        getCollection().add(index, instance);
    }

    @Nullable
    @Override
    public T remove(int index) {
        return getCollection().remove(index);
    }

    @Override
    public int firstIndexOf(T instance) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        ListIterator<T> it = listIterator();
        while (it.hasNext()) {
            int i = it.nextIndex();
            if (matcher.matchesExactly(it.next(), instance)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(T instance) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        ListIterator<T> it = listIterator(size());
        while (it.hasPrevious()) {
            int i = it.previousIndex();
            if (matcher.matchesExactly(it.previous(), instance)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return getCollection().listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int offset) {
        return getCollection().listIterator(offset);
    }

    @Override
    public IIngredientList<T, M> subList(int fromIndex, int toIndex) {
        return new IngredientList<>(getComponent(), getCollection().subList(fromIndex, toIndex));
    }
}
