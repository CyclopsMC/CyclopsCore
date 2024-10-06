package org.cyclops.cyclopscore.datastructure;

import it.unimi.dsi.fastutil.ints.IntIterator;

import java.util.PrimitiveIterator;

/**
 * @author rubensworks
 */
public class WrappedIntIterator implements PrimitiveIterator.OfInt {

    private final IntIterator it;

    public WrappedIntIterator(IntIterator it) {
        this.it = it;
    }

    @Override
    public int nextInt() {
        return it.nextInt();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public Integer next() {
        return it.next();
    }

}
