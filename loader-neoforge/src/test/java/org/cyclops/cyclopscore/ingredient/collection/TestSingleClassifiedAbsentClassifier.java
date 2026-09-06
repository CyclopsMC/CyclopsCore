package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Queries on a single-classified collection whose classifier holds nothing.
 *
 * A match condition that covers the category type can only match instances sharing the query's
 * classifier, so an absent classifier means an empty result. These lookups must say so directly
 * rather than falling back on a scan over every classifier, which is both slower and grows with
 * the size of the whole collection instead of with the size of one classifier.
 *
 * @author rubensworks
 */
public class TestSingleClassifiedAbsentClassifier {

    private static final int GROUP = ComplexStack.Match.GROUP;
    private static final int GROUP_META = ComplexStack.Match.GROUP | ComplexStack.Match.META;

    private static final ComplexStack A01 = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack A12 = new ComplexStack(ComplexStack.Group.A, 1, 2, null);
    private static final ComplexStack B01 = new ComplexStack(ComplexStack.Group.B, 0, 1, null);
    /**
     * Nothing of this group is ever added.
     */
    private static final ComplexStack C01 = new ComplexStack(ComplexStack.Group.C, 0, 1, null);

    /**
     * Counts every time an inner collection is asked to iterate, so that a fallback scan over all
     * classifiers becomes visible rather than only slow.
     */
    private AtomicInteger innerIterations;

    private IngredientCollectionSingleClassified<ComplexStack, Integer, ?, IIngredientCollectionMutable<ComplexStack, Integer>> collection;
    private IngredientMapSingleClassified<ComplexStack, Integer, String, ?> map;

    @Before
    public void before() {
        this.innerIterations = new AtomicInteger();

        this.collection = new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new CountingSet(this.innerIterations),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0));
        this.collection.add(A01);
        this.collection.add(A12);
        this.collection.add(B01);

        this.map = new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new CountingMap(this.innerIterations),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0));
        this.map.put(A01, "a01");
        this.map.put(A12, "a12");
        this.map.put(B01, "b01");

        this.innerIterations.set(0);
    }

    @Test
    public void testCollectionCountAbsentClassifier() {
        assertThat(collection.count(C01, GROUP), is(0));
        assertThat(collection.count(C01, GROUP_META), is(0));
        assertThat(innerIterations.get(), is(0));
    }

    @Test
    public void testCollectionCountPresentClassifier() {
        assertThat(collection.count(A01, GROUP), is(2));
        assertThat(collection.count(B01, GROUP), is(1));
        assertThat(collection.count(A01, GROUP_META), is(1));
    }

    @Test
    public void testMapCountKeyAbsentClassifier() {
        assertThat(map.countKey(C01, GROUP), is(0));
        assertThat(map.countKey(C01, GROUP_META), is(0));
        assertThat(innerIterations.get(), is(0));
    }

    @Test
    public void testMapCountKeyPresentClassifier() {
        assertThat(map.countKey(A01, GROUP), is(2));
        assertThat(map.countKey(B01, GROUP), is(1));
        assertThat(map.countKey(A01, GROUP_META), is(1));
    }

    @Test
    public void testMapContainsKeyAbsentClassifier() {
        assertThat(map.containsKey(C01, GROUP), is(false));
        assertThat(map.containsKey(C01, GROUP_META), is(false));
        assertThat(innerIterations.get(), is(0));
    }

    @Test
    public void testMapContainsKeyPresentClassifier() {
        assertThat(map.containsKey(A01, GROUP), is(true));
        assertThat(map.containsKey(A01, GROUP_META), is(true));
    }

    @Test
    public void testMapGetAllAbsentClassifier() {
        assertThat(map.getAll(C01, GROUP).isEmpty(), is(true));
        assertThat(map.getAll(C01, GROUP_META).isEmpty(), is(true));
        assertThat(innerIterations.get(), is(0));
    }

    @Test
    public void testMapGetAllPresentClassifier() {
        assertThat(map.getAll(A01, GROUP).size(), is(2));
        assertThat(map.getAll(B01, GROUP).size(), is(1));
        assertThat(map.getAll(A01, GROUP_META).size(), is(1));
    }

    @Test
    public void testMapKeySetAbsentClassifier() {
        assertThat(map.keySet(C01, GROUP).isEmpty(), is(true));
        assertThat(map.keySet(C01, GROUP_META).isEmpty(), is(true));
        assertThat(innerIterations.get(), is(0));
    }

    @Test
    public void testMapKeySetPresentClassifier() {
        assertThat(map.keySet(A01, GROUP).size(), is(2));
        assertThat(map.keySet(B01, GROUP).size(), is(1));
        assertThat(map.keySet(A01, GROUP_META).size(), is(1));
    }

    /**
     * A match condition that does not cover the category type can not be answered by the
     * classifier, so it still has to visit the inner collections.
     */
    @Test
    public void testMatchConditionOutsideClassifierStillScans() {
        assertThat(map.countKey(C01, ComplexStack.Match.META), is(2));
        assertThat(innerIterations.get() > 0, is(true));
    }

    private static class CountingSet extends IngredientHashSet<ComplexStack, Integer> {

        private final AtomicInteger counter;

        public CountingSet(AtomicInteger counter) {
            super(IngredientComponentStubs.COMPLEX);
            this.counter = counter;
        }

        @Override
        public Iterator<ComplexStack> iterator() {
            this.counter.incrementAndGet();
            return super.iterator();
        }
    }

    private static class CountingMap extends IngredientHashMap<ComplexStack, Integer, String> {

        private final AtomicInteger counter;

        public CountingMap(AtomicInteger counter) {
            super(IngredientComponentStubs.COMPLEX);
            this.counter = counter;
        }

        @Override
        public Iterator<Map.Entry<ComplexStack, String>> iterator() {
            this.counter.incrementAndGet();
            return super.iterator();
        }

        @Override
        public IngredientSet<ComplexStack, Integer> keySet() {
            this.counter.incrementAndGet();
            return super.keySet();
        }

        @Override
        public Collection<String> values() {
            this.counter.incrementAndGet();
            return super.values();
        }
    }
}
