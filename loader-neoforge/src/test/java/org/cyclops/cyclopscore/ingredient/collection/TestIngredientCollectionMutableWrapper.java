package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientCollectionMutableWrapper {

    private IIngredientCollectionMutable<Integer, Boolean> collection;
    private IIngredientCollectionMutable<Integer, Boolean> innerCollection;

    @Before
    public void beforeEach() {
        IngredientHashSet<Integer, Boolean> innerCollection = new IngredientHashSet<>(IngredientComponentStubs.SIMPLE);
        innerCollection.add(0);
        innerCollection.add(1);
        innerCollection.add(2);
        this.innerCollection = innerCollection;
        this.collection = new IngredientCollectionMutableWrapper<>(innerCollection);
    }

    @Test
    public void testAdd() {
        assertThat(innerCollection.contains(4), is(false));
        assertThat(collection.add(4), is(true));
        assertThat(innerCollection.contains(4), is(true));
    }

    @Test
    public void testAddAll() {
        assertThat(innerCollection.contains(4), is(false));
        assertThat(innerCollection.contains(5), is(false));
        assertThat(innerCollection.contains(6), is(false));
        assertThat(collection.addAll(Lists.newArrayList(4, 5, 6)), is(true));
        assertThat(innerCollection.contains(4), is(true));
        assertThat(innerCollection.contains(5), is(true));
        assertThat(innerCollection.contains(6), is(true));
    }

    @Test
    public void testRemove() {
        assertThat(innerCollection.contains(2), is(true));
        assertThat(collection.remove(2), is(true));
        assertThat(innerCollection.contains(2), is(false));
    }

    @Test
    public void testRemoveMatch() {
        assertThat(innerCollection.contains(2), is(true));
        assertThat(innerCollection.contains(1), is(true));
        assertThat(collection.removeAll(2, false), is(3));
        assertThat(innerCollection.contains(2), is(false));
        assertThat(innerCollection.contains(1), is(false));
    }

    @Test
    public void testRemoveAll() {
        assertThat(innerCollection.contains(0), is(true));
        assertThat(innerCollection.contains(1), is(true));
        assertThat(innerCollection.contains(2), is(true));
        assertThat(collection.removeAll(Lists.newArrayList(0, 1, 2)), is(3));
        assertThat(innerCollection.contains(0), is(false));
        assertThat(innerCollection.contains(1), is(false));
        assertThat(innerCollection.contains(2), is(false));
    }

    @Test
    public void testRemoveAllMatch() {
        assertThat(innerCollection.contains(0), is(true));
        assertThat(innerCollection.contains(1), is(true));
        assertThat(innerCollection.contains(2), is(true));
        assertThat(collection.removeAll(Lists.newArrayList(0, 1), false), is(3));
        assertThat(innerCollection.contains(0), is(false));
        assertThat(innerCollection.contains(1), is(false));
        assertThat(innerCollection.contains(2), is(false));
    }

    @Test
    public void testClear() {
        collection.clear();
        assertThat(innerCollection.isEmpty(), is(true));
    }

}
