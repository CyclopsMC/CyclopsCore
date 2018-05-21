package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * An iterator that filters over instance map entries based on an instance with a match condition.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <V> The value type.
 */
public class FilteredIngredientMapIterator<T, M, V> implements Iterator<Map.Entry<T, V>> {

    private final Iterator<Map.Entry<T, V>> iterator;
    private final IIngredientMatcher<T, M> matcher;
    private final T instance;
    private final M matchCondition;

    private Map.Entry<T, V> next;

    /**
     * Create a new instance.
     * @param iterator An iterator with instances.
     * @param matcher A matcher for the type of instances.
     * @param instance An instance to match.
     * @param matchCondition A match condition to filter by.
     */
    public FilteredIngredientMapIterator(Iterator<Map.Entry<T, V>> iterator, IIngredientMatcher<T, M> matcher,
                                         T instance, M matchCondition) {
        this.iterator = iterator;
        this.matcher = matcher;
        this.instance = instance;
        this.matchCondition = matchCondition;
        this.next = null;
    }

    protected Map.Entry<T, V> findNext() {
        while (iterator.hasNext()) {
            Map.Entry<T, V> next = iterator.next();
            if (matcher.matches(instance, next.getKey(), matchCondition)) {
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
    public Map.Entry<T, V> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Tried reading a finished FilteredIngredientMapIterator");
        }
        Map.Entry<T, V> next = this.next;
        this.next = null;
        return next;
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
