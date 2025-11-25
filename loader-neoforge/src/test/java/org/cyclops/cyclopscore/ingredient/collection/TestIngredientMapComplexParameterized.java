package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.AbstractMap;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientMapComplexParameterized {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    public Stream<Arguments> data() {
        return Stream.of(
                        new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                        new IngredientHashMap<>(IngredientComponentStubs.COMPLEX, 3),
                        new IngredientHashMap<>(IngredientComponentStubs.COMPLEX, new IngredientHashMap<>(IngredientComponentStubs.COMPLEX)),
                        new IngredientHashMap<>(IngredientComponentStubs.COMPLEX, Maps.newHashMap()),
                        new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX),
                        new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX, new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX)),
                        new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX, Maps.newTreeMap()),
                        new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)),
                        new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(1)),
                        new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(2)),
                        new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(3)),
                        new IngredientMapMultiClassified<>(IngredientComponentStubs.COMPLEX,
                                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX))
                ).map(collection -> {
                    collection.clear();
                    collection.put(CA01_, 0);
                    collection.put(CB02_, 1);
                    collection.put(CA91B, 9);
                    return collection;
                })
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testContainsMatch(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
        assertThat(collection.containsKey(CA01_, ComplexStack.Match.EXACT), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.EXACT), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.EXACT), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.EXACT), is(false));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.ANY), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.ANY), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.ANY), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.ANY), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.GROUP), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.GROUP), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.GROUP), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.GROUP), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.META), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.META), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.META), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.META), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.AMOUNT), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.TAG), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(false));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(false));

        assertThat(collection.containsKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
        assertThat(collection.containsKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testCount(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
        assertThat(collection.countKey(CA01_, ComplexStack.Match.EXACT), is(1));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.EXACT), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.EXACT), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.EXACT), is(0));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.ANY), is(3));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.ANY), is(3));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.ANY), is(3));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.ANY), is(3));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.GROUP), is(2));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.GROUP), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.GROUP), is(2));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.GROUP), is(2));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.META), is(2));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.META), is(2));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.META), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.META), is(2));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.AMOUNT), is(2));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.AMOUNT), is(2));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.AMOUNT), is(2));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.TAG), is(2));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.TAG), is(2));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.TAG), is(1));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(2));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(2));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(2));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(2));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(2));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(0));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(0));

        assertThat(collection.countKey(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        assertThat(collection.countKey(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testIteratorMatch(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.EXACT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.EXACT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.EXACT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.EXACT)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.ANY)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1), new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.ANY)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1), new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.ANY)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1), new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.ANY)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1), new AbstractMap.SimpleEntry<>(CA91B, 9))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CA91B, 9))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.META)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.META)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.META)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.META)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CA91B, 9))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CA91B, 9))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0), new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.iterator(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA01_, 0))));
        assertThat(Sets.newHashSet(collection.iterator(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CB02_, 1))));
        assertThat(Sets.newHashSet(collection.iterator(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
        assertThat(Sets.newHashSet(collection.iterator(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(CA91B, 9))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGetAll(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.EXACT)), is(Sets.newHashSet(0)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.EXACT)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.EXACT)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.EXACT)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.ANY)), is(Sets.newHashSet(0, 1, 9)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.ANY)), is(Sets.newHashSet(0, 1, 9)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.ANY)), is(Sets.newHashSet(0, 1, 9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.ANY)), is(Sets.newHashSet(0, 1, 9)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.GROUP)), is(Sets.newHashSet(0, 9)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.GROUP)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.GROUP)), is(Sets.newHashSet(0, 9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.GROUP)), is(Sets.newHashSet(0, 9)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.META)), is(Sets.newHashSet(0, 1)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.META)), is(Sets.newHashSet(0, 1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.META)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.META)), is(Sets.newHashSet(0, 1)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(0, 9)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(0, 9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(0, 9)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.TAG)), is(Sets.newHashSet(0, 1)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.TAG)), is(Sets.newHashSet(0, 1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.TAG)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.TAG)), is(Sets.newHashSet(9)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(0)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(0)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(0, 9)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(0, 9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(0, 9)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(0)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(9)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(0)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(0)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(0, 1)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(0, 1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(0)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(9)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(0)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(0)));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(0)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.getAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(0)));
        assertThat(Sets.newHashSet(collection.getAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(1)));
        assertThat(Sets.newHashSet(collection.getAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(9)));
        assertThat(Sets.newHashSet(collection.getAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(9)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testKeySetMatch(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.EXACT)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.EXACT)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.EXACT)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.EXACT)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.ANY)), is(Sets.newHashSet(CA01_, CB02_, CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.ANY)), is(Sets.newHashSet(CA01_, CB02_, CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.ANY)), is(Sets.newHashSet(CA01_, CB02_, CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.ANY)), is(Sets.newHashSet(CA01_, CB02_, CA91B)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.GROUP)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.GROUP)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.GROUP)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.GROUP)), is(Sets.newHashSet(CA01_, CA91B)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.META)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.META)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.META)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.META)), is(Sets.newHashSet(CA01_, CB02_)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META)), is(Sets.newHashSet(CA01_)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_, CA91B)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT)), is(Sets.newHashSet(CA01_)));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG)), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.keySet(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(collection.keySet(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CB02_)));
        assertThat(Sets.newHashSet(collection.keySet(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(collection.keySet(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG)), is(Sets.newHashSet(CA91B)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllExact(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllAny(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllGroup(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllMeta(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllAmount(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllTag(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllGroupMeta(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllGroupAmount(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllGroupTag(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllMetaAmount(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllMetaTag(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllAmountTag(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllGroupMetaAmount(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllGroupMetaTag(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllGroupAmountTag(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
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
    public void testRemoveAllIterable(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(CA01_, CB02_, CA91B, CA01B), ComplexStack.Match.EXACT), is(3));
        assertThat(collection.size(), is(0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveAllIterableAy(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
        assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(CA01_, CB02_), ComplexStack.Match.ANY), is(3));
        assertThat(collection.size(), is(0));
    }

}
