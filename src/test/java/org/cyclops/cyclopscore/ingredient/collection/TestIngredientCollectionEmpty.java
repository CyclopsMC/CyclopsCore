package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestIngredientCollectionEmpty {

    public IngredientCollectionEmpty<Integer, Boolean> collection;

    @Before
    public void beforeEach() {
        collection = new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE);
    }

    @Test
    public void testGetComponent() {
        assertThat(collection.getComponent(), is(IngredientComponentStubs.SIMPLE));
    }

    @Test
    public void testSize() {
        assertThat(collection.size(), is(0));
    }

    @Test
    public void testContains() {
        assertThat(collection.contains(0), is(false));
    }

    @Test
    public void testContainsMatch() {
        assertThat(collection.contains(0, null), is(false));
    }

    @Test
    public void testCount() {
        assertThat(collection.count(0, null), is(0));
    }

    @Test
    public void testIterator() {
        assertThat(collection.iterator().hasNext(), is(false));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(collection.iterator(0, null).hasNext(), is(false));
    }

    @Test
    public void testEquals() {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals(new IngredientCollectionEmpty<>(IngredientComponentStubs.COMPLEX)), is(false));
        assertThat(collection.equals(null), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE)), is(true));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX)), is(false));
    }

    @Test
    public void testHashCode() {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.COMPLEX).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX).hashCode())));
    }

    @Test
    public void testToString() {
        assertThat(collection.toString(), equalTo("[]"));
    }

}
