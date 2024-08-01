package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientCollectionWrapper {

    private IIngredientCollection<Integer, Boolean> collection;
    private IIngredientCollection<Integer, Boolean> innerCollection;

    @Before
    public void beforeEach() {
        IngredientHashSet<Integer, Boolean> innerCollection = new IngredientHashSet<>(IngredientComponentStubs.SIMPLE);
        innerCollection.add(0);
        innerCollection.add(1);
        innerCollection.add(2);
        this.innerCollection = innerCollection;
        this.collection = new IngredientCollectionWrapper<>(innerCollection);
    }

    @Test
    public void testContains() {
        assertThat(collection.contains(-1), is(innerCollection.contains(-1)));
        assertThat(collection.contains(0), is(innerCollection.contains(0)));
        assertThat(collection.contains(1), is(innerCollection.contains(1)));
        assertThat(collection.contains(2), is(innerCollection.contains(2)));
    }

    @Test
    public void testContainsMatch() {
        assertThat(collection.contains(-1, true), is(innerCollection.contains(-1, true)));
        assertThat(collection.contains(0, true), is(innerCollection.contains(0, true)));
        assertThat(collection.contains(1, true), is(innerCollection.contains(1, true)));
        assertThat(collection.contains(2, true), is(innerCollection.contains(2, true)));
        assertThat(collection.contains(-1, false), is(innerCollection.contains(-1, false)));
        assertThat(collection.contains(0, false), is(innerCollection.contains(0, false)));
        assertThat(collection.contains(1, false), is(innerCollection.contains(1, false)));
        assertThat(collection.contains(2, false), is(innerCollection.contains(2, false)));
    }

    @Test
    public void testContainsAll() {
        assertThat(collection.containsAll(Lists.newArrayList(0, 1, 2)), is(innerCollection.containsAll(Lists.newArrayList(0, 1, 2))));
        assertThat(collection.containsAll(Lists.newArrayList(-1, 1, 2)), is(innerCollection.containsAll(Lists.newArrayList(-1, 1, 2))));
    }

    @Test
    public void testContainsAllMatch() {
        assertThat(collection.containsAll(Lists.newArrayList(0, 1, 2), true), is(innerCollection.containsAll(Lists.newArrayList(0, 1, 2), true)));
        assertThat(collection.containsAll(Lists.newArrayList(-1, 1, 2), true), is(innerCollection.containsAll(Lists.newArrayList(-1, 1, 2), true)));
        assertThat(collection.containsAll(Lists.newArrayList(0, 1, 2), false), is(innerCollection.containsAll(Lists.newArrayList(0, 1, 2), false)));
        assertThat(collection.containsAll(Lists.newArrayList(-1, 1, 2), false), is(innerCollection.containsAll(Lists.newArrayList(-1, 1, 2), false)));
    }

    @Test
    public void testCount() {
        assertThat(collection.count(-1, true), is(innerCollection.count(-1, true)));
        assertThat(collection.count(-1, false), is(innerCollection.count(-1, false)));
        assertThat(collection.count(0, true), is(innerCollection.count(0, true)));
        assertThat(collection.count(0, false), is(innerCollection.count(0, false)));
    }

    @Test
    public void testGetComponent() {
        assertThat(collection.getComponent(), is(innerCollection.getComponent()));
    }

    @Test
    public void testSize() {
        assertThat(collection.size(), is(innerCollection.size()));
    }

    @Test
    public void testIsEmpty() {
        assertThat(collection.isEmpty(), is(innerCollection.isEmpty()));
    }

    @Test
    public void testIteratorMatches() {
        assertThat(Iterators.elementsEqual(collection.iterator(-1, true), innerCollection.iterator(-1, true)), is(true));
        assertThat(Iterators.elementsEqual(collection.iterator(-1, false), innerCollection.iterator(-1, false)), is(true));
        assertThat(Iterators.elementsEqual(collection.iterator(0, true), innerCollection.iterator(0, true)), is(true));
        assertThat(Iterators.elementsEqual(collection.iterator(0, false), innerCollection.iterator(0, false)), is(true));
    }

    @Test
    public void testIterator() {
        assertThat(Iterators.elementsEqual(collection.iterator(), innerCollection.iterator()), is(true));
    }

    @Test
    public void testHashCode() {
        assertThat(collection.hashCode(), is(innerCollection.hashCode()));
    }

    @Test
    public void testEquals() {
        assertThat(collection, is(innerCollection));
    }

    @Test
    public void testToString() {
        assertThat(collection.toString(), is(innerCollection.toString()));
    }

}
