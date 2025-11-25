package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientCollectionsParameterized<T, M> {

    private static final ComplexStack C0 = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack C1 = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack C2 = new ComplexStack(ComplexStack.Group.A, 10, 1, ComplexStack.Tag.B);
    private static final ComplexStack C3 = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    public Stream<Arguments> data() {
        return Stream.of(
                /*  0 */ Arguments.of(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3),
                /*  1 */ Arguments.of(new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3),
                /*  2 */ Arguments.of(new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3),
                /*  3 */ Arguments.of(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3),
                /*  4 */ Arguments.of(new IngredientCollectionSingleClassified<>(IngredientComponentStubs.SIMPLE,
                () -> new IngredientHashSet<>(IngredientComponentStubs.SIMPLE),
                IngredientComponentStubs.SIMPLE.getCategoryTypes().get(0)), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3),
                /*  5 */ Arguments.of(new IngredientCollectionMultiClassified<>(IngredientComponentStubs.SIMPLE,
                () -> new IngredientHashSet<>(IngredientComponentStubs.SIMPLE)), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3),

                /*  6 */ Arguments.of(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3),
                /*  7 */ Arguments.of(new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3),
                /*  8 */ Arguments.of(new IngredientTreeSet<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3),
                /*  9 */ Arguments.of(new IngredientHashSet<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3),
                /* 10 */ Arguments.of(new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3),
                /* 11 */ Arguments.of(new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(1)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3),
                /* 12 */ Arguments.of(new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(2)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3),
                /* 13 */ Arguments.of(new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(3)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3),
                /* 14 */ Arguments.of(new IngredientCollectionMultiClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGetComponent(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.getComponent(), is(component));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testEmpty(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection.toString(), equalTo("[]"));
        assertThat(collection.hashCode(), is(IngredientCollections.emptyCollection(collection.getComponent()).hashCode()));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddSingle(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(b), is(true));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveSingle(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(b), is(true));

        assertThat(collection.remove(b), is(true));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveRepeatedSingle(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(b), is(true));

        assertThat(collection.remove(b), is(true));
        assertThat(collection.remove(b), is(false));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddRemoveCycleSingle(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(b), is(true));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.remove(b), is(false));

        assertThat(collection.add(b), is(true));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.remove(b), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));

        assertThat(collection.remove(b), is(true));
        assertThat(collection.remove(b), is(false));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddMultiple(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(a), is(true));
        assertThat(collection.add(b), is(true));
        assertThat(collection.add(c), is(true));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddAll(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.addAll(Lists.newArrayList(a, b, c)), is(true));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveMultiple(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(a), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.add(b), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.add(c), is(true));
        assertThat(collection.size(), is(3));

        assertThat(collection.remove(a), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllMultiple(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(a), is(true));
        assertThat(collection.add(b), is(true));
        assertThat(collection.add(c), is(true));

        assertThat(collection.removeAll(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAll(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.addAll(Lists.newArrayList(a, b, c)), is(true));
        assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(a, b, c, d)), is(3));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllMatch(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.addAll(Lists.newArrayList(a, b, c)), is(true));
        assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(3));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveRepeatedMultiple(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(a), is(true));
        assertThat(collection.add(b), is(true));
        assertThat(collection.add(c), is(true));

        assertThat(collection.remove(a), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), is(false));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), is(false));
        assertThat(collection.size(), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddRemoveCycleMultiple(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(a), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.add(b), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.add(c), is(true));
        assertThat(collection.size(), is(3));
        assertThat(collection.remove(a), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), is(false));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));

        assertThat(collection.add(a), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.add(b), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.add(c), is(true));
        assertThat(collection.size(), is(3));
        assertThat(collection.remove(a), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), is(false));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));

        assertThat(collection.add(a), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.add(b), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.add(c), is(true));
        assertThat(collection.size(), is(3));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection.remove(a), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), is(false));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testClearEmpty(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));

        collection.clear();

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testClearNonEmpty(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(b), is(true));
        assertThat(collection.add(c), is(true));
        assertThat(collection.add(d), is(true));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));

        collection.clear();

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContains(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.contains(a), is(false));
        assertThat(collection.contains(b), is(false));
        assertThat(collection.contains(c), is(false));
        assertThat(collection.contains(d), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.contains(a), is(false));
        assertThat(collection.contains(b), is(true));
        assertThat(collection.contains(c), is(false));
        assertThat(collection.contains(d), is(false));

        assertThat(collection.add(c), is(true));

        assertThat(collection.contains(a), is(false));
        assertThat(collection.contains(b), is(true));
        assertThat(collection.contains(c), is(true));
        assertThat(collection.contains(d), is(false));

        assertThat(collection.add(d), is(true));

        assertThat(collection.contains(a), is(false));
        assertThat(collection.contains(b), is(true));
        assertThat(collection.contains(c), is(true));
        assertThat(collection.contains(d), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsMatch(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.contains(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(c), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(d), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsMatchAny(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.contains(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.add(c), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.add(d), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsAll(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.containsAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(c)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d)), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d)), is(false));

        assertThat(collection.add(c), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d)), is(false));

        assertThat(collection.add(d), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d)), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsAllMatch(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(c), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(d), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsAllMatchAny(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.add(c), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.add(d), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsAllMatchAnyEmpty(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));

        assertThat(collection.containsAll(Lists.newArrayList(), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIterator(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        Iterator<T> it0 = collection.iterator();
        assertThat(it0.hasNext(), is(false));

        assertThat(collection.add(a), is(true));
        Iterator<T> it1 = collection.iterator();
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next(), is(a));
        assertThat(it1.hasNext(), is(false));

        assertThat(collection.add(b), is(true));
        ArrayList<T> c2 = Lists.newArrayList(collection.iterator());
        assertThat(c2.size(), is(2));
        assertThat(c2.containsAll(Lists.newArrayList(a, b)), is(true));

        assertThat(collection.add(c), is(true));
        ArrayList<T> c3 = Lists.newArrayList(collection.iterator());
        assertThat(c3.size(), is(3));
        assertThat(c3.containsAll(Lists.newArrayList(a, b, c)), is(true));

        assertThat(collection.add(d), is(true));
        ArrayList<T> c4 = Lists.newArrayList(collection.iterator());
        assertThat(c4.size(), is(4));
        assertThat(c4.containsAll(Lists.newArrayList(a, b, c, d)), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmptyCollectionNext(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        Iterator<T> it = collection.iterator();
        Assertions.assertThrows(RuntimeException.class, it::next);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemove(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(a), is(true));
        assertThat(collection.size(), is(1));

        Iterator<T> it1 = collection.iterator();
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next(), is(a));
        it1.remove();
        assertThat(it1.hasNext(), is(false));

        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveBeforeStart(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.add(a), is(true));
        Iterator<T> it = collection.iterator();
        Assertions.assertThrows(RuntimeException.class, it::remove);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveBeforeStartEmpty(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        Iterator<T> it = collection.iterator();
        Assertions.assertThrows(RuntimeException.class, it::remove);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveMultiple(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        Iterator<T> it = collection.iterator();
        Assertions.assertThrows(RuntimeException.class, () -> {
            it.remove();
            it.remove();
            it.remove();
        });
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testStream(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.stream().collect(Collectors.toSet()), is(Sets.newHashSet(collection)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testParallelStream(
            IIngredientCollectionMutable<T, M> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d
    ) {
        assertThat(collection.parallelStream().collect(Collectors.toSet()), is(Sets.newHashSet(collection)));
    }

}
