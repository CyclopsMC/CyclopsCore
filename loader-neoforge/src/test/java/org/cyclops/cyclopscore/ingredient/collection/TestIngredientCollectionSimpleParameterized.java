package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientCollectionSimpleParameterized {

    public Stream<Arguments> data() {
        return Stream.of(
                new IngredientHashSet<>(IngredientComponentStubs.SIMPLE),
                new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, 3),
                new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()),
                new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE),
                new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()),
                new IngredientArrayList<>(IngredientComponentStubs.SIMPLE),
                new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 3),
                new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()),
                new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, new Integer[0]),
                new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE),
                new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList())
        ).map(collection -> {
            collection.clear();
            collection.add(0);
            collection.add(1);
            collection.add(2);
            return collection;
        }).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsMatch(IIngredientCollectionMutable<Integer, Boolean> collection) {
        assertThat(collection.contains(0, true), is(true));
        assertThat(collection.contains(1, true), is(true));
        assertThat(collection.contains(2, true), is(true));
        assertThat(collection.contains(3, true), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testCount(IIngredientCollectionMutable<Integer, Boolean> collection) {
        assertThat(collection.count(0, true), is(1));
        assertThat(collection.count(1, true), is(1));
        assertThat(collection.count(2, true), is(1));
        assertThat(collection.count(3, true), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorMatch(IIngredientCollectionMutable<Integer, Boolean> collection) {
        assertThat(Lists.newArrayList(collection.iterator(0, true)), is(Lists.newArrayList(0)));
        assertThat(Lists.newArrayList(collection.iterator(1, true)), is(Lists.newArrayList(1)));
        assertThat(Lists.newArrayList(collection.iterator(2, true)), is(Lists.newArrayList(2)));
        assertThat(Lists.newArrayList(collection.iterator(3, true)), is(Lists.newArrayList()));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testToString(IIngredientCollectionMutable<Integer, Boolean> collection) {
        assertThat(collection.toString(), equalTo("[0, 1, 2]"));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAll(IIngredientCollectionMutable<Integer, Boolean> collection) {
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
    public void testRemoveAllAny(IIngredientCollectionMutable<Integer, Boolean> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(3, false), is(3));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllIterable(IIngredientCollectionMutable<Integer, Boolean> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(0, 1, 2, 3), true), is(3));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllIterableAny(IIngredientCollectionMutable<Integer, Boolean> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(0, 1), false), is(3));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testToArray(IIngredientCollectionMutable<Integer, Boolean> collection) {
        assertThat(Arrays.equals(collection.toArray(), new Integer[]{0, 1, 2}), is(true));
    }

}
