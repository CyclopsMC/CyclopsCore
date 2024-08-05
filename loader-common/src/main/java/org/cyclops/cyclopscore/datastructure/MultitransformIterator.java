package org.cyclops.cyclopscore.datastructure;

import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * An iterator that takes an iterator as input,
 * and applies each element of that iterator to a function that produces another iterator.
 * The elements of all resulting iterators are produced by this.
 *
 * This is implemented in a lazy manner,
 * meaning that the function will only be applied to elements when they are needed.
 *
 * @param <T> The type over which the resulting iterator will iterate over.
 * @param <F> The output of the input iterator, and the input of the function.
 */
public class MultitransformIterator<T, F> implements Iterator<T> {

    private final Iterator<? extends F> it;
    private final Function<F, Iterator<? extends T>> transformer;

    private Iterator<? extends T> lastIt = Iterators.forArray();
    private Iterator<? extends T> removalIt = null;

    public MultitransformIterator(Iterator<? extends F> it, Function<F, Iterator<? extends T>> transformer) {
        this.it = it;
        this.transformer = transformer;
    }

    @Override
    public boolean hasNext() {
        if (lastIt.hasNext()) {
            return true;
        }

        while (it.hasNext()) {
            lastIt = this.transformer.apply(it.next());
            if (lastIt.hasNext()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        removalIt = lastIt;
        return lastIt.next();
    }

    @Override
    public void remove() {
        if (removalIt == null) {
            throw new IllegalStateException("Attempted to call #remove before #next");
        }
        removalIt.remove();
    }

    /**
     * Flatten the given nested iterator.
     * @param it An iterator over iterators.
     * @param <T> The type of the inner iterators.
     * @return A flattened iterator.
     */
    public static <T> Iterator<T> flattenIterableIterator(Iterator<? extends Iterable<T>> it) {
        return new MultitransformIterator<>(it, Iterable::iterator);
    }
}
