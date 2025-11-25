package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientMapSimple {

    public Stream<Arguments> data() {
        return Stream.of(
                new IngredientHashMap<>(IngredientComponentStubs.SIMPLE),
                new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, 3),
                new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, new IngredientHashMap<>(IngredientComponentStubs.SIMPLE)),
                new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, Maps.newHashMap()),
                new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE),
                new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE, new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE)),
                new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE, Maps.newTreeMap())
                ).map(collection -> {
            collection.clear();
            collection.put(0, 0);
            collection.put(1, 10);
            collection.put(2, 20);
            return collection;
        }).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testEquals(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
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

    @ParameterizedTest
    @MethodSource("data")
    public void testHashCode(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientHashMap<>(IngredientComponentStubs.COMPLEX).hashCode())));
        IngredientHashMap<Integer, Boolean, Integer> simpleMap = new IngredientHashMap<>(IngredientComponentStubs.SIMPLE);
        simpleMap.put(0, 0);
        simpleMap.put(1, 10);
        simpleMap.put(2, 20);
        assertThat(collection.hashCode(), is(simpleMap.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX).hashCode())));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorNext(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(0, true);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(new AbstractMap.SimpleEntry<>(0, 0)));
        assertThat(it.hasNext(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorNextTooMany(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(0, true);
        assertThat(it.next(), is(new AbstractMap.SimpleEntry<>(0, 0)));
        Assertions.assertThrows(NoSuchElementException.class, it::next);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemove(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(0, true);
        assertThat(it.next(), is(new AbstractMap.SimpleEntry<>(0, 0)));
        it.remove();
        assertThat(collection.containsKey(0), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveTooMany(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(0, true);
        assertThat(it.next(), is(new AbstractMap.SimpleEntry<>(0, 0)));
        Assertions.assertThrows(IllegalStateException.class, () -> {
            it.remove();
            it.remove();
        });
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmpty(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(4, true);
        assertThat(it.hasNext(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorNextEmpty(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(4, true);
        Assertions.assertThrows(RuntimeException.class, it::next);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveEmpty(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(4, true);
        Assertions.assertThrows(RuntimeException.class, it::remove);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveNoNext(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        Iterator<Map.Entry<Integer, Integer>> it = collection.iterator(0, true);
        Assertions.assertThrows(RuntimeException.class, it::remove);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsMatch(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        assertThat(collection.containsKey(0, true), is(true));
        assertThat(collection.containsKey(1, true), is(true));
        assertThat(collection.containsKey(2, true), is(true));
        assertThat(collection.containsKey(3, true), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testCount(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        assertThat(collection.countKey(0, true), is(1));
        assertThat(collection.countKey(1, true), is(1));
        assertThat(collection.countKey(2, true), is(1));
        assertThat(collection.countKey(3, true), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorMatch(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        assertThat(Lists.newArrayList(collection.iterator(0, true)), is(Lists.newArrayList(new AbstractMap.SimpleEntry<>(0, 0))));
        assertThat(Lists.newArrayList(collection.iterator(1, true)), is(Lists.newArrayList(new AbstractMap.SimpleEntry<>(1, 10))));
        assertThat(Lists.newArrayList(collection.iterator(2, true)), is(Lists.newArrayList(new AbstractMap.SimpleEntry<>(2, 20))));
        assertThat(Lists.newArrayList(collection.iterator(3, true)), is(Lists.newArrayList()));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testToString(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        assertThat(collection.toString(), equalTo("[{0,0}, {1,10}, {2,20}]"));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAll(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(3, true), is(0));
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(2, true), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(1, true), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(0, true), is(1));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllAny(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(3, false), is(3));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllIterable(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(0, 1, 2, 3), true), is(3));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllIterableAny(IIngredientMapMutable<Integer, Boolean, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(0, 1), false), is(3));
        assertThat(collection.size(), is(0));
    }

}
