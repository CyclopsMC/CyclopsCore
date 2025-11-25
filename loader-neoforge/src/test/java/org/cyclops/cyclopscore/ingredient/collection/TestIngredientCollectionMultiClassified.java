package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientCollectionMultiClassified {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    public Stream<Arguments> data() {
        return Stream.of(
                new IngredientCollectionMultiClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX))
        ).map(collection -> {
            collection.clear();
            collection.add(CA01_);
            collection.add(CB02_);
            collection.add(CA91B);
            return collection;
        }).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddMultiple(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
        assertThat(collection.add(CA01_), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.add(CB02_), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.add(CA91B), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.add(CA01B), is(true));
        assertThat(collection.size(), is(4));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testEquals(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
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

    @ParameterizedTest
    @MethodSource("data")
    public void testHashCode(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, CA01_, CB02_, CA91B).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE).hashCode())));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIterator(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
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

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmpty(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
        Iterator<ComplexStack> it = collection.iterator(CA01B, ComplexStack.Match.EXACT);
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmptyCollection(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.EXACT);
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmptyCollectionNextExact(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.EXACT);
        Assertions.assertThrows(RuntimeException.class, it::next);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmptyCollectionNextAny(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.ANY);
        Assertions.assertThrows(RuntimeException.class, it::next);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmptyCollectionNextGroup(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        Assertions.assertThrows(RuntimeException.class, it::next);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemove(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
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

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveBeforeStart(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        Assertions.assertThrows(RuntimeException.class, it::remove);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveBeforeStartEmpty(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
        Iterator<ComplexStack> it = collection.iterator(CA01B, ComplexStack.Match.EXACT);
        Assertions.assertThrows(RuntimeException.class, it::remove);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveMultiple(IngredientCollectionMultiClassified<ComplexStack, Integer> collection) {
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        Assertions.assertThrows(RuntimeException.class, () -> {
            it.remove();
            it.remove();
            it.remove();
        });
    }

}
