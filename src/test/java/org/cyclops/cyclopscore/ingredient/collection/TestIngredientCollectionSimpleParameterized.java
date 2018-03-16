package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientCollectionSimpleParameterized {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new IngredientHashSet<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, 3) },
                { new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()) },
                { new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()) },
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 3) },
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()) },
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, new Integer[0]) },
                { new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()) },
        });
    }

    @Parameterized.Parameter
    public IIngredientCollectionMutable<Integer, Boolean> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.add(0);
        collection.add(1);
        collection.add(2);
    }

    @Test
    public void testContainsMatch() {
        assertThat(collection.contains(0, true), is(true));
        assertThat(collection.contains(1, true), is(true));
        assertThat(collection.contains(2, true), is(true));
        assertThat(collection.contains(3, true), is(false));
    }

    @Test
    public void testCount() {
        assertThat(collection.count(0, true), is(1));
        assertThat(collection.count(1, true), is(1));
        assertThat(collection.count(2, true), is(1));
        assertThat(collection.count(3, true), is(0));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(Lists.newArrayList(collection.iterator(0, true)), is(Lists.newArrayList(0)));
        assertThat(Lists.newArrayList(collection.iterator(1, true)), is(Lists.newArrayList(1)));
        assertThat(Lists.newArrayList(collection.iterator(2, true)), is(Lists.newArrayList(2)));
        assertThat(Lists.newArrayList(collection.iterator(3, true)), is(Lists.newArrayList()));
    }

    @Test
    public void testToString() {
        assertThat(collection.toString(), equalTo("[0, 1, 2]"));
    }

    @Test
    public void testRemoveAll() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(3, true), is(0));
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(2, true), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(1, true), is(1));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(0, true), is(1));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllAny() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(3, false), is(3));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllIterable() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(0, 1, 2, 3), true), is(3));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllIterableAny() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(0, 1), false), is(3));
        Assert.assertThat(collection.size(), is(0));
    }

}
