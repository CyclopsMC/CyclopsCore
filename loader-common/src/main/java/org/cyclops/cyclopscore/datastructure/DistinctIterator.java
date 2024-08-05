package org.cyclops.cyclopscore.datastructure;

import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * An iterator that wraps around another iterator and only outputs unique items.
 * @param <T> The type to iterate over.
 */
public class DistinctIterator<T> implements Iterator<T> {

    private final Iterator<T> it;
    private final Set<T> passed;

    private T next;

    public DistinctIterator(Iterator<T> it) {
        this(it, false);
    }

    public DistinctIterator(Iterator<T> it, boolean identity) {
        this.it = it;
        this.passed = identity ? Sets.newIdentityHashSet() : Sets.newHashSet();
    }

    @Override
    public boolean hasNext() {
        if (next != null) {
            return true;
        }

        while (it.hasNext()) {
            next = it.next();
            if (passed.add(next)) {
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

        T ret = next;
        next = null;
        return ret;
    }

    @Override
    public void remove() {
        // Not implemented as removal with duplicates can have various different requirements
        throw new UnsupportedOperationException("remove");
    }
}
