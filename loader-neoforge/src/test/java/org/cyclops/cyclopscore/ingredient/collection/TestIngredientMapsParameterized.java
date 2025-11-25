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

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientMapsParameterized<T, M, V> {

    private static final ComplexStack C0 = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack C1 = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack C2 = new ComplexStack(ComplexStack.Group.A, 10, 1, ComplexStack.Tag.B);
    private static final ComplexStack C3 = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    public Stream<Arguments> data() {
        return Stream.of(
                /*  0 */ Arguments.of(new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3, 0, 1, 2, 3),
                /*  1 */ Arguments.of(new IngredientHashMap<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3, 0, 1, 2, 3),
                /*  2 */ Arguments.of(new IngredientMapSingleClassified<>(IngredientComponentStubs.SIMPLE,
                () -> new IngredientHashMap<>(IngredientComponentStubs.SIMPLE),
                IngredientComponentStubs.SIMPLE.getCategoryTypes().get(0)), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3, 0, 1, 2, 3),
                /*  3 */ Arguments.of(new IngredientMapMultiClassified<>(IngredientComponentStubs.SIMPLE,
                () -> new IngredientHashMap<>(IngredientComponentStubs.SIMPLE)), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3, 0, 1, 2, 3),

                /*  4 */ Arguments.of(new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3),
                /*  5 */ Arguments.of(new IngredientHashMap<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3),
                /*  6 */ Arguments.of(new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3),
                /*  7 */ Arguments.of(new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(1)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3),
                /*  8 */ Arguments.of(new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(2)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3),
                /*  9 */ Arguments.of(new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(3)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3),
                /* 10 */ Arguments.of(new IngredientMapMultiClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGetComponent(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.getComponent(), is(component));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testEmpty(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));

        assertThat(collection.toString(), equalTo("[]"));
        assertThat(collection.hashCode(), is(IngredientCollections.emptyCollection(collection.getComponent()).hashCode()));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testPutSingle(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(1));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveSingle(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.remove(b), is(vb));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveRepeatedSingle(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.remove(b), is(vb));
        assertThat(collection.remove(b), nullValue());

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testPutRemoveCycleSingle(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.remove(b), nullValue());

        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.remove(b), nullValue());

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(1));

        assertThat(collection.remove(b), is(vb));
        assertThat(collection.remove(b), nullValue());

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testPutMultiple(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(3));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testPutRepeatedKey(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.get(a), is(va));
        assertThat(collection.put(a, vb), is(va));
        assertThat(collection.get(a), is(vb));
        assertThat(collection.put(a, vc), is(vb));
        assertThat(collection.get(a), is(vc));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testPutAll(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        IngredientHashMap<T, M, V> subIngredientMap = new IngredientHashMap<>(component);
        subIngredientMap.put(a, va);
        subIngredientMap.put(b, vb);
        subIngredientMap.put(c, vc);
        assertThat(collection.putAll(subIngredientMap), is(3));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(3));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGetAll(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        collection.put(a, va);
        collection.put(b, vb);
        collection.put(c, vc);

        assertThat(Sets.newHashSet(collection.getAll(a, collection.getComponent().getMatcher().getExactMatchCondition())), is(Sets.newHashSet(va)));
        assertThat(Sets.newHashSet(collection.getAll(b, collection.getComponent().getMatcher().getExactMatchCondition())), is(Sets.newHashSet(vb)));
        assertThat(Sets.newHashSet(collection.getAll(c, collection.getComponent().getMatcher().getExactMatchCondition())), is(Sets.newHashSet(vc)));
        assertThat(Sets.newHashSet(collection.getAll(d, collection.getComponent().getMatcher().getExactMatchCondition())), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.getAll(a, collection.getComponent().getMatcher().getAnyMatchCondition())), is(Sets.newHashSet(va, vb, vc)));
        assertThat(Sets.newHashSet(collection.getAll(b, collection.getComponent().getMatcher().getAnyMatchCondition())), is(Sets.newHashSet(va, vb, vc)));
        assertThat(Sets.newHashSet(collection.getAll(c, collection.getComponent().getMatcher().getAnyMatchCondition())), is(Sets.newHashSet(va, vb, vc)));
        assertThat(Sets.newHashSet(collection.getAll(d, collection.getComponent().getMatcher().getAnyMatchCondition())), is(Sets.newHashSet(va, vb, vc)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveMultiple(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.put(c, vc), nullValue());
        assertThat(collection.size(), is(3));

        assertThat(collection.remove(a), is(va));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(vc));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllMultiple(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(3));

        assertThat(collection.removeAll(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAll(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        IngredientHashMap<T, M, V> subIngredientMap = new IngredientHashMap<>(component);
        subIngredientMap.put(a, va);
        subIngredientMap.put(b, vb);
        subIngredientMap.put(c, vc);
        assertThat(collection.putAll(subIngredientMap), is(3));
        assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(a, b, c, d)), is(3));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllMatch(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        IngredientHashMap<T, M, V> subIngredientMap = new IngredientHashMap<>(component);
        subIngredientMap.put(a, va);
        subIngredientMap.put(b, vb);
        subIngredientMap.put(c, vc);
        assertThat(collection.putAll(subIngredientMap), is(3));
        assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(3));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveRepeatedMultiple(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.remove(a), is(va));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(vc));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), nullValue());
        assertThat(collection.size(), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testPutRemoveCycleMultiple(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.put(c, vc), nullValue());
        assertThat(collection.size(), is(3));
        assertThat(collection.remove(a), is(va));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(vc));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));

        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.put(c, vc), nullValue());
        assertThat(collection.size(), is(3));
        assertThat(collection.remove(a), is(va));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(vc));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));

        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.put(c, vc), nullValue());
        assertThat(collection.size(), is(3));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(3));

        assertThat(collection.remove(a), is(va));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(vc));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testClearEmpty(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
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
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.put(c, vc), nullValue());
        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));

        collection.clear();

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsKey(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.containsKey(a), is(false));
        assertThat(collection.containsKey(b), is(false));
        assertThat(collection.containsKey(c), is(false));
        assertThat(collection.containsKey(d), is(false));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.containsKey(a), is(false));
        assertThat(collection.containsKey(b), is(true));
        assertThat(collection.containsKey(c), is(false));
        assertThat(collection.containsKey(d), is(false));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.containsKey(a), is(false));
        assertThat(collection.containsKey(b), is(true));
        assertThat(collection.containsKey(c), is(true));
        assertThat(collection.containsKey(d), is(false));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.containsKey(a), is(false));
        assertThat(collection.containsKey(b), is(true));
        assertThat(collection.containsKey(c), is(true));
        assertThat(collection.containsKey(d), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsValue(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.containsValue(va), is(false));
        assertThat(collection.containsValue(vb), is(false));
        assertThat(collection.containsValue(vc), is(false));
        assertThat(collection.containsValue(vd), is(false));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.containsValue(va), is(false));
        assertThat(collection.containsValue(vb), is(true));
        assertThat(collection.containsValue(vc), is(false));
        assertThat(collection.containsValue(vd), is(false));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.containsValue(va), is(false));
        assertThat(collection.containsValue(vb), is(true));
        assertThat(collection.containsValue(vc), is(true));
        assertThat(collection.containsValue(vd), is(false));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.containsValue(va), is(false));
        assertThat(collection.containsValue(vb), is(true));
        assertThat(collection.containsValue(vc), is(true));
        assertThat(collection.containsValue(vd), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testKeySet(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.keySet(), is(new IngredientHashSet<>(component)));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.keySet(), is(new IngredientHashSet<>(component, Lists.newArrayList(b))));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.keySet(), is(new IngredientHashSet<>(component, Lists.newArrayList(b, c))));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.keySet(), is(new IngredientHashSet<>(component, Lists.newArrayList(b, c, d))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testValues(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(Sets.newHashSet(collection.values()), is(Sets.newHashSet()));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(Sets.newHashSet(collection.values()), is(Sets.newHashSet(vb)));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(Sets.newHashSet(collection.values()), is(Sets.newHashSet(vb, vc)));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(Sets.newHashSet(collection.values()), is(Sets.newHashSet(vb, vc, vd)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testEntrySet(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.entrySet(), is(Sets.newHashSet()));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.entrySet(), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(b, vb))));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.entrySet(), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(b, vb), new AbstractMap.SimpleEntry<>(c, vc))));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.entrySet(), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(b, vb), new AbstractMap.SimpleEntry<>(c, vc), new AbstractMap.SimpleEntry<>(d, vd))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmptyCollectionNext(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        Iterator<Map.Entry<T, V>> it = collection.iterator();
        Assertions.assertThrows(RuntimeException.class, it::next);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemove(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.size(), is(1));

        Iterator<Map.Entry<T, V>> it1 = collection.iterator();
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next(), is(new AbstractMap.SimpleEntry<>(a, va)));
        it1.remove();
        assertThat(it1.hasNext(), is(false));

        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveBeforeStart(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        assertThat(collection.put(a, va), nullValue());
        Iterator<Map.Entry<T, V>> it = collection.iterator();
        Assertions.assertThrows(RuntimeException.class, it::remove);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveBeforeStartEmpty(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        Iterator<Map.Entry<T, V>> it = collection.iterator();
        Assertions.assertThrows(RuntimeException.class, it::remove);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveMultiple(
            IIngredientMapMutable<T, M, V> collection, IngredientComponent<T, M> component,
            T a, T b, T c, T d,
            V va, V vb, V vc, V vd
    ) {
        Iterator<Map.Entry<T, V>> it = collection.iterator();
        Assertions.assertThrows(RuntimeException.class, () -> {
            it.remove();
            it.remove();
            it.remove();
        });
    }

}
