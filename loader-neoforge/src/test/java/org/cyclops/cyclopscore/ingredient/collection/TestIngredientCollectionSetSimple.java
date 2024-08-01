package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientCollectionSetSimple {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new IngredientHashSet<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, 3) },
                { new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()) },
                { new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()) },
        });
    }

    @Parameterized.Parameter
    public IngredientSet<Integer, Boolean> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.add(0);
        collection.add(1);
        collection.add(2);
    }

    @Test
    public void testEquals() {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals("abc"), is(false));
        assertThat(collection.equals(new IngredientCollectionEmpty<>(IngredientComponentStubs.COMPLEX)), is(false));
        assertThat(collection.equals(null), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2))), is(true));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 3))), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1))), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.COMPLEX)), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1, 2)), is(false));
    }

    @Test
    public void testHashCode() {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.COMPLEX).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1, 2).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX).hashCode())));
    }

    @Test
    public void testIteratorNext() {
        Iterator<Integer> it = collection.iterator(0, true);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(0));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNextTooMany() {
        Iterator<Integer> it = collection.iterator(0, true);
        assertThat(it.next(), is(0));
        it.next();
    }

    @Test
    public void testIteratorRemove() {
        Iterator<Integer> it = collection.iterator(0, true);
        assertThat(it.next(), is(0));
        it.remove();
        assertThat(collection.contains(0), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorRemoveTooMany() {
        Iterator<Integer> it = collection.iterator(0, true);
        assertThat(it.next(), is(0));
        it.remove();
        it.remove();
    }

    @Test
    public void testIteratorEmpty() {
        Iterator<Integer> it = collection.iterator(4, true);
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorNextEmpty() {
        Iterator<Integer> it = collection.iterator(4, true);
        it.next();
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorRemoveEmpty() {
        Iterator<Integer> it = collection.iterator(4, true);
        it.remove();
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorRemoveNoNext() {
        Iterator<Integer> it = collection.iterator(0, true);
        it.remove();
    }

}
