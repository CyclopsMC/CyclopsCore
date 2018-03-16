package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that filters over instances based on an instance with a match condition.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class FilteredIngredientIterator<T, M> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final IIngredientMatcher<T, M> matcher;
    private final T instance;
    private final M matchCondition;

    private T next;

    /**
     * Create a new instance.
     * @param iterable An iterable with instances.
     * @param matcher A matcher for the type of instances.
     * @param instance An instance to match.
     * @param matchCondition A match condition to filter by.
     */
    public FilteredIngredientIterator(Iterable<T> iterable, IIngredientMatcher<T, M> matcher,
                                      T instance, M matchCondition) {
        this.iterator = iterable.iterator();
        this.matcher = matcher;
        this.instance = instance;
        this.matchCondition = matchCondition;
        this.next = null;
    }

    protected T findNext() {
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (matcher.matches(instance, next, matchCondition)) {
                return next;
            }
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        if (next == null) {
            this.next = findNext();
        }
        return next != null;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Tried reading a finished FilteredIngredientIterator");
        }
        T next = this.next;
        this.next = null;
        return next;
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
