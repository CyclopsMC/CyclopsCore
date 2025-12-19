package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;
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
public class TestIngredientCollectionSingleClassified {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    public Stream<Arguments> data() {
        return Stream.<Pair<IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?>, IngredientComponentCategoryType<ComplexStack, Integer, ?>>>of(
                Pair.of(new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)), IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)),
                Pair.of(new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(1)), IngredientComponentStubs.COMPLEX.getCategoryTypes().get(1)),
                Pair.of(new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(2)), IngredientComponentStubs.COMPLEX.getCategoryTypes().get(2)),
                Pair.of(new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(3)), IngredientComponentStubs.COMPLEX.getCategoryTypes().get(3))
                ).map(pair -> {
            pair.getLeft().clear();
            pair.getLeft().add(CA01_);
            pair.getLeft().add(CB02_);
            pair.getLeft().add(CA91B);
            return Arguments.of(pair.getLeft(), pair.getRight());
        });
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGetCategoryType(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        assertThat(collection.getCategoryType(), is(categoryType));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddMultiple(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
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
    public void testEquals(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(null), is(false));
        IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> c0 = new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX), categoryType);
        IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> c1 = new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX), new IngredientComponentCategoryType<>(
                        Identifier.parse("dummy"), ComplexStack.Group.class, true, ComplexStack::getGroup, ComplexStack.Match.GROUP, false));
        IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> c2 = new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX), categoryType);
        c0.addAll(Lists.newArrayList(CA01_, CB02_, CA91B));
        c2.add(CA01B);
        assertThat(collection.equals(c0), is(true));
        assertThat(collection.equals(c1), is(false));
        assertThat(collection.equals(c2), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2))), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testHashCode(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, CA01_, CB02_, CA91B).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE).hashCode())));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIterator(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
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
    public void testIteratorEmpty(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        Iterator<ComplexStack> it = collection.iterator(CA01B, ComplexStack.Match.EXACT);
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmptyCollection(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.EXACT);
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmptyCollectionNextExact(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.EXACT);
        Assertions.assertThrows(RuntimeException.class, it::next);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmptyCollectionNextAny(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.ANY);
        Assertions.assertThrows(RuntimeException.class, it::next);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorEmptyCollectionNextGroup(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        collection.clear();
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        Assertions.assertThrows(RuntimeException.class, it::next);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemove(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
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
    public void testIteratorRemoveBeforeStart(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        Assertions.assertThrows(RuntimeException.class, it::remove);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveBeforeStartEmpty(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        Iterator<ComplexStack> it = collection.iterator(CA01B, ComplexStack.Match.EXACT);
        Assertions.assertThrows(RuntimeException.class, it::remove);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorRemoveMultiple(
            IngredientCollectionSingleClassified<ComplexStack, Integer, ?, ?> collection,
            IngredientComponentCategoryType<ComplexStack, Integer, ?> categoryType
    ) {
        Iterator<ComplexStack> it = collection.iterator(CA01_, ComplexStack.Match.GROUP);
        Assertions.assertThrows(RuntimeException.class, () -> {
            it.remove();
            it.remove();
            it.remove();
        });
    }

}
