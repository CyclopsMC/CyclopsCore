package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientCollectionComplexParameterized {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    public Stream<Arguments> data() {
        return Stream.of(
                new IngredientArrayList<>(IngredientComponentStubs.COMPLEX),
                new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, 3),
                new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()),
                new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, new ComplexStack[0]),
                new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX),
                new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()),
                new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, 3),
                new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()),
                new IngredientTreeSet<>(IngredientComponentStubs.COMPLEX),
                new IngredientTreeSet<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()),
                new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)),
                new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(1)),
                new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(2)),
                new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(3)),
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
    public void testContainsMatch(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.contains(CA01_, ComplexStack.Match.EXACT), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.EXACT), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.EXACT), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.EXACT), is(false));

        assertThat(collection.contains(CA01_, ComplexStack.Match.ANY), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.ANY), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.ANY), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.ANY), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.GROUP), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.GROUP), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.GROUP), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.GROUP), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.META), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.META), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.META), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.META), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.AMOUNT), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.TAG), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(false));

        assertThat(collection.contains(CA01_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));

        assertThat(collection.contains(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(false));

        assertThat(collection.contains(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.contains(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testCount(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.count(CA01_, ComplexStack.Match.EXACT), is(1));
        assertThat(collection.count(CB02_, ComplexStack.Match.EXACT), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.EXACT), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.EXACT), is(0));

        assertThat(collection.count(CA01_, ComplexStack.Match.ANY), is(3));
        assertThat(collection.count(CB02_, ComplexStack.Match.ANY), is(3));
        assertThat(collection.count(CA91B, ComplexStack.Match.ANY), is(3));
        assertThat(collection.count(CA01B, ComplexStack.Match.ANY), is(3));

        assertThat(collection.count(CA01_, ComplexStack.Match.GROUP), is(2));
        assertThat(collection.count(CB02_, ComplexStack.Match.GROUP), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.GROUP), is(2));
        assertThat(collection.count(CA01B, ComplexStack.Match.GROUP), is(2));

        assertThat(collection.count(CA01_, ComplexStack.Match.META), is(2));
        assertThat(collection.count(CB02_, ComplexStack.Match.META), is(2));
        assertThat(collection.count(CA91B, ComplexStack.Match.META), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.META), is(2));

        assertThat(collection.count(CA01_, ComplexStack.Match.AMOUNT), is(2));
        assertThat(collection.count(CB02_, ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.AMOUNT), is(2));
        assertThat(collection.count(CA01B, ComplexStack.Match.AMOUNT), is(2));

        assertThat(collection.count(CA01_, ComplexStack.Match.TAG), is(2));
        assertThat(collection.count(CB02_, ComplexStack.Match.TAG), is(2));
        assertThat(collection.count(CA91B, ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.TAG), is(1));

        assertThat(collection.count(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        assertThat(collection.count(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));

        assertThat(collection.count(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(2));
        assertThat(collection.count(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(2));
        assertThat(collection.count(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(2));

        assertThat(collection.count(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));

        assertThat(collection.count(CA01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.count(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));

        assertThat(collection.count(CA01_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(2));
        assertThat(collection.count(CB02_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(2));
        assertThat(collection.count(CA91B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(0));

        assertThat(collection.count(CA01_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CB02_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));

        assertThat(collection.count(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.count(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));

        assertThat(collection.count(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(0));

        assertThat(collection.count(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.count(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorMatch(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.EXACT)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.EXACT)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.EXACT)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.EXACT)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.ANY)), is(Sets.newHashSet(CA01_, CB02_, CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.ANY)), is(Sets.newHashSet(CA01_, CB02_, CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.ANY)), is(Sets.newHashSet(CA01_, CB02_, CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.ANY)), is(Sets.newHashSet(CA01_, CB02_, CA91B)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP)), is(Sets.newHashSet(CA01_, CA91B)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.META)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.META)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.META)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.META)), is(Sets.newHashSet(CA01_, CB02_)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(CA01_)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_)));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllExact(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.EXACT), is(0));
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.EXACT), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.EXACT), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.EXACT), is(1));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllAny(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.ANY), is(3));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.ANY), is(0));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.ANY), is(0));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.ANY), is(0));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllGroup(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP), is(2));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP), is(0));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP), is(0));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllMeta(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.META), is(2));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.META), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.META), is(0));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.META), is(0));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllAmount(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.AMOUNT), is(2));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.AMOUNT), is(0));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.AMOUNT), is(0));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllTag(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.TAG), is(0));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.TAG), is(2));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.TAG), is(0));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllGroupMeta(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(0));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllGroupAmount(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(2));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(0));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(0));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllGroupTag(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(0));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllMetaAmount(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(0));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllMetaTag(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(0));
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(2));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(0));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllAmountTag(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(0));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllGroupMetaAmount(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(0));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllGroupMetaTag(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(0));
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllGroupAmountTag(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(0));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllIterable(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(CA01_, CB02_, CA91B, CA01B), ComplexStack.Match.EXACT), is(3));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllIterableAy(IIngredientCollectionMutable<ComplexStack, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(CA01_, CB02_), ComplexStack.Match.ANY), is(3));
        assertThat(collection.size(), is(0));
    }

}
