package org.cyclops.cyclopscore.datastructure;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestMultitransformIterator {

    @Test
    public void testEmpty() {
        MultitransformIterator<Object, Object> it = new MultitransformIterator<>(Iterators.forArray(),
                o -> null);

        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testDuplicate() {
        MultitransformIterator<Integer, Integer> it = new MultitransformIterator<>(Iterators.forArray(1, 2),
                o -> Iterators.forArray(o, o));

        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(2));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(2));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testDuplicateSelective() {
        MultitransformIterator<Integer, Integer> it = new MultitransformIterator<>(Iterators.forArray(1, 2, 3),
                o -> o == 2 ? Iterators.forArray() : Iterators.forArray(o, o));

        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(3));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(3));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoNext() {
        new MultitransformIterator<>(Iterators.forArray(), o -> null).next();
    }

    @Test
    public void testRemove() {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3);
        MultitransformIterator<Integer, Integer> it = new MultitransformIterator<>(Iterators.forArray(1),
                o -> list.iterator());

        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        it.remove();
        assertThat(list, is(Lists.newArrayList(2, 3)));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(2));
        it.remove();
        assertThat(list, is(Lists.newArrayList(3)));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(3));
        it.remove();
        assertThat(list, is(Lists.newArrayList()));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoRemove() {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3);
        MultitransformIterator<Integer, Integer> it = new MultitransformIterator<>(Iterators.forArray(1),
                o -> list.iterator());
        it.remove();
    }

    @Test
    public void testFlatten() {
        Iterator<Integer> it = MultitransformIterator.flattenIterableIterator(Iterators.forArray(Lists.newArrayList(1, 2), Lists.newArrayList(3, 4, 5), Lists.newArrayList(6)));

        assertThat(it.next(), is(1));
        assertThat(it.next(), is(2));
        assertThat(it.next(), is(3));
        assertThat(it.next(), is(4));
        assertThat(it.next(), is(5));
        assertThat(it.next(), is(6));
    }
}
