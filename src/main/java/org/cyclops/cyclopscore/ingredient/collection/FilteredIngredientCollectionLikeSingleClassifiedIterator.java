package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A filtered iterator over a collection that has the {@link IIngredientCollectionLikeSingleClassifiedTrait} trait.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <I> The type that can be iterated over. This is typically just T.
 * @param <C> A classifier type.
 * @param <L> The collection-like type that is being used to store classified partitions.
 */
public class FilteredIngredientCollectionLikeSingleClassifiedIterator<T, M, I, C, L extends IIngredientCollectionLike<T, M, I>> implements Iterator<I> {

    private final IIngredientCollectionLikeSingleClassifiedTrait<T, M, I, C, L> classifiedCollection;
    private final Iterator<I> iterator;
    private final IIngredientMatcher<T, M> matcher;
    private final T instance;
    private final M matchCondition;

    private I next;

    /**
     * Create a new instance.
     * @param classifiedCollection The classified collection this iterator is created for.
     * @param matcher A matcher for the type of instances.
     * @param instance An instance to match.
     * @param matchCondition A match condition to filter by.
     */
    public FilteredIngredientCollectionLikeSingleClassifiedIterator(IIngredientCollectionLikeSingleClassifiedTrait<T, M, I, C, L> classifiedCollection,
                                                                    IIngredientMatcher<T, M> matcher, T instance, M matchCondition) {
        this.classifiedCollection = classifiedCollection;
        this.iterator = this.classifiedCollection.iterator();
        this.matcher = matcher;
        this.instance = instance;
        this.matchCondition = matchCondition;
        this.next = null;
    }

    protected I findNext() {
        while (iterator.hasNext()) {
            I next = iterator.next();
            if (matcher.matches(instance, classifiedCollection.getInstance(next), matchCondition)) {
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
    public I next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Tried reading a finished FilteredIngredientMapIterator");
        }
        I next = this.next;
        this.next = null;
        return next;
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
