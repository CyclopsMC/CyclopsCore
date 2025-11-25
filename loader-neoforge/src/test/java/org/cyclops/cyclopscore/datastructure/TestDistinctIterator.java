package org.cyclops.cyclopscore.datastructure;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class TestDistinctIterator {

    @Test
    public void testEmpty() {
        DistinctIterator<Object> it = new DistinctIterator<>(Iterators.forArray());

        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testEmptyDistinct() {
        DistinctIterator<Object> it = new DistinctIterator<>(Iterators.forArray(), true);

        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testListDistinct() {
        DistinctIterator<Integer> it = new DistinctIterator<>(Lists.newArrayList(1, 2, 3).iterator());

        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(2));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(3));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testListDuplicates() {
        DistinctIterator<Integer> it = new DistinctIterator<>(Lists.newArrayList(1, 1, 2, 2, 3, 3, 3).iterator());

        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(2));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(3));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testListDuplicatesMixed() {
        DistinctIterator<Integer> it = new DistinctIterator<>(Lists.newArrayList(1, 2, 1, 3, 2, 3, 2, 1, 2).iterator());

        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(2));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(3));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testNoNext() {
        Assertions.assertThrows(NoSuchElementException.class, () -> new DistinctIterator<>(Iterators.forArray()).next());
    }

    @Test
    public void testNoRemove() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> new DistinctIterator<>(Iterators.forArray()).remove());
    }
}
