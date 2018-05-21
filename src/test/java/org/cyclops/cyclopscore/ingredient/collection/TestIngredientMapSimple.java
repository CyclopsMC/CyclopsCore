package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientMapSimple {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new IngredientHashMap<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, 3) },
                { new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, new IngredientHashMap<>(IngredientComponentStubs.SIMPLE)) },
                { new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, Maps.newHashMap()) },
                { new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE, new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE)) },
                { new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE, Maps.newTreeMap()) },
        });
    }

    @Parameterized.Parameter
    public IIngredientMapMutable<Integer, Boolean, Integer> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.put(0, 0);
        collection.put(1, 10);
        collection.put(2, 20);
    }

    @Test
    public void testEquals() {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals("abc"), is(false));
        assertThat(collection.equals(new IngredientHashMap<>(IngredientComponentStubs.COMPLEX)), is(false));
        assertThat(collection.equals(null), is(false));
        HashMap<IngredientInstanceWrapper<Integer, Boolean>, Integer> subMap0 = Maps.newHashMap();
        subMap0.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 0), 0);
        subMap0.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 1), 10);
        subMap0.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 2), 20);
        assertThat(collection.equals(new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, subMap0)), is(true));
        HashMap<IngredientInstanceWrapper<Integer, Boolean>, Integer> subMap1 = Maps.newHashMap();
        subMap1.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 0), 0);
        subMap1.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 1), 10);
        subMap1.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 3), 30);
        assertThat(collection.equals(new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, subMap1)), is(false));
        HashMap<IngredientInstanceWrapper<Integer, Boolean>, Integer> subMap2 = Maps.newHashMap();
        subMap2.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 0), 0);
        subMap2.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 1), 10);
        assertThat(collection.equals(new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, subMap2)), is(false));
        assertThat(collection.equals(new IngredientHashMap<>(IngredientComponentStubs.COMPLEX)), is(false));
    }

    @Test
    public void testHashCode() {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientHashMap<>(IngredientComponentStubs.COMPLEX).hashCode())));
        IngredientHashMap<Integer, Boolean, Integer> simpleMap = new IngredientHashMap<>(IngredientComponentStubs.SIMPLE);
        simpleMap.put(0, 0);
        simpleMap.put(1, 10);
        simpleMap.put(2, 20);
        assertThat(collection.hashCode(), is(simpleMap.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX).hashCode())));
    }

    @Test
    public void testIteratorNext() {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(0, true);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new AbstractMap.SimpleEntry<>(0, 0)));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNextTooMany() {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(0, true);
        assertThat(it.next(), is(new AbstractMap.SimpleEntry<>(0, 0)));
        it.next();
    }

    @Test
    public void testIteratorRemove() {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(0, true);
        assertThat(it.next(), is(new AbstractMap.SimpleEntry<>(0, 0)));
        it.remove();
        assertThat(collection.containsKey(0), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorRemoveTooMany() {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(0, true);
        assertThat(it.next(), is(new AbstractMap.SimpleEntry<>(0, 0)));
        it.remove();
        it.remove();
    }

    @Test
    public void testIteratorEmpty() {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(4, true);
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorNextEmpty() {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(4, true);
        it.next();
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorRemoveEmpty() {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(4, true);
        it.remove();
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorRemoveNoNext() {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(0, true);
        it.remove();
    }

    @Test
    public void testContainsMatch() {
        assertThat(collection.containsKey(0, true), is(true));
        assertThat(collection.containsKey(1, true), is(true));
        assertThat(collection.containsKey(2, true), is(true));
        assertThat(collection.containsKey(3, true), is(false));
    }

    @Test
    public void testCount() {
        assertThat(collection.countKey(0, true), is(1));
        assertThat(collection.countKey(1, true), is(1));
        assertThat(collection.countKey(2, true), is(1));
        assertThat(collection.countKey(3, true), is(0));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(Lists.newArrayList(collection.iterator(0, true)), is(Lists.newArrayList(new AbstractMap.SimpleEntry<>(0, 0))));
        assertThat(Lists.newArrayList(collection.iterator(1, true)), is(Lists.newArrayList(new AbstractMap.SimpleEntry<>(1, 10))));
        assertThat(Lists.newArrayList(collection.iterator(2, true)), is(Lists.newArrayList(new AbstractMap.SimpleEntry<>(2, 20))));
        assertThat(Lists.newArrayList(collection.iterator(3, true)), is(Lists.newArrayList()));
    }

    @Test
    public void testToString() {
        assertThat(collection.toString(), equalTo("[{0,0}, {1,10}, {2,20}]"));
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
