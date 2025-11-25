package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientCollectionSetSimple {

    public Stream<Arguments> data() {
        return Stream.of(
                new IngredientHashSet<>(IngredientComponentStubs.SIMPLE),
                new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, 3),
                new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()),
                new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE),
                new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList())
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
    public void testEquals(IngredientSet<Integer, Boolean> collection) {
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

    @ParameterizedTest
    @MethodSource("data")
    public void testHashCode(IngredientSet<Integer, Boolean> collection) {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.COMPLEX).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1, 2).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX).hashCode())));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorNext(IngredientSet<Integer, Boolean> collection) {
        Iterator<Integer> it = collection.iterator(0, true);
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(0));
        assertThat(it.hasNext(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorNextTooMany(IngredientSet<Integer, Boolean> collection) {
        Iterator<Integer> it = collection.iterator(0, true);
        assertThat(it.next(), is(0));
        Assertions.assertThrows(NoSuchElementException.class, () -> it.next());
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemove(IngredientSet<Integer, Boolean> collection) {
        Iterator<Integer> it = collection.iterator(0, true);
        assertThat(it.next(), is(0));
        it.remove();
        assertThat(collection.contains(0), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveTooMany(IngredientSet<Integer, Boolean> collection) {
        Iterator<Integer> it = collection.iterator(0, true);
        assertThat(it.next(), is(0));
        Assertions.assertThrows(RuntimeException.class, () -> {
            it.remove();
            it.remove();
        });
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmpty(IngredientSet<Integer, Boolean> collection) {
        Iterator<Integer> it = collection.iterator(4, true);
        assertThat(it.hasNext(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorNextEmpty(IngredientSet<Integer, Boolean> collection) {
        Iterator<Integer> it = collection.iterator(4, true);
        Assertions.assertThrows(RuntimeException.class, () -> it.next());
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveEmpty(IngredientSet<Integer, Boolean> collection) {
        Iterator<Integer> it = collection.iterator(4, true);
        Assertions.assertThrows(RuntimeException.class, () -> it.remove());
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveNoNext(IngredientSet<Integer, Boolean> collection) {
        Iterator<Integer> it = collection.iterator(0, true);
        Assertions.assertThrows(RuntimeException.class, () -> it.remove());
    }

}
