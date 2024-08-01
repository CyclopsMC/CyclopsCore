package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientCollectionMultiClassified {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new IngredientCollectionMultiClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX)) }
        });
    }

    @Parameterized.Parameter(0)
    public IngredientCollectionMultiClassified<ComplexStack, Integer> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.add(CA01_);
        collection.add(CB02_);
        collection.add(CA91B);
    }

    @Test
    public void testAddMultiple() {
        assertThat(collection.add(CA01_), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.add(CB02_), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.add(CA91B), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.add(CA01B), is(true));
        assertThat(collection.size(), is(4));
    }

    @Test
    public void testEquals() {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(null), is(false));
        IngredientCollectionMultiClassified<ComplexStack, Integer> c0 = new IngredientCollectionMultiClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX));
        IngredientCollectionMultiClassified<ComplexStack, Integer> c1 = new IngredientCollectionMultiClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX));
        c0.addAll(Lists.newArrayList(CA01_, CB02_, CA91B));
        c1.add(CA01B);
        assertThat(collection.equals(c0), is(true));
        assertThat(collection.equals(c1), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2))), is(false));
    }

    @Test
    public void testHashCode() {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, CA01_, CB02_, CA91B).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE).hashCode())));
    }

    @Test
    public void testIterator() {
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        HashSet<ComplexStack> results = Sets.newHashSet();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(results.add(it.next()), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(results.add(it.next()), is(true));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
        assertThat(results, equalTo(Sets.newHashSet(CA01_, CA91B)));
    }

    @Test
    public void testIteratorEmpty() {
        Iterator<ComplexStack> it = collection.iterator(CA01B, ComplexStack.Match.EXACT);
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testIteratorEmptyCollection() {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.EXACT);
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorEmptyCollectionNextExact() {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.EXACT);
        it.next();
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorEmptyCollectionNextAny() {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.ANY);
        it.next();
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorEmptyCollectionNextGroup() {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        it.next();
    }

    @Test
    public void testIteratorRemove() {
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        HashSet<ComplexStack> results = Sets.newHashSet();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(results.add(it.next()), is(true));
        assertThat(collection.size(), is(3));
        it.remove();
        assertThat(collection.size(), is(2));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(results.add(it.next()), is(true));
        it.remove();
        assertThat(collection.size(), is(1));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
        assertThat(results, equalTo(Sets.newHashSet(CA01_, CA91B)));
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorRemoveBeforeStart() {
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        it.remove();
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorRemoveBeforeStartEmpty() {
        Iterator<ComplexStack> it = collection.iterator(CA01B, ComplexStack.Match.EXACT);
        it.remove();
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorRemoveMultiple() {
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        it.remove();
        it.remove();
        it.remove();
    }

}
