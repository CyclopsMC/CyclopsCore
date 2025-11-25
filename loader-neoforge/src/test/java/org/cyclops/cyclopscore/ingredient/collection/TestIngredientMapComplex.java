package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientMapComplex {

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
                new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX, Maps.newTreeMap())
                ).map(collection -> {
            collection.clear();
            collection.put(CA01_, 0);
            collection.put(CB02_, 0);
            collection.put(CA91B, 9);
            return collection;
        }).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testEquals(IIngredientMapMutable<ComplexStack, Boolean, Integer> collection) {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals("abc"), is(false));
        assertThat(collection.equals(new IngredientHashMap<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(null), is(false));
        HashMap<IngredientInstanceWrapper<ComplexStack, Integer>, Integer> subMap0 = Maps.newHashMap();
        subMap0.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.COMPLEX, CA01_), 0);
        subMap0.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.COMPLEX, CB02_), 0);
        subMap0.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.COMPLEX, CA91B), 9);
        assertThat(collection.equals(new IngredientHashMap<>(IngredientComponentStubs.COMPLEX, subMap0)), is(true));
        assertThat(collection.equals(new IngredientHashMap<>(IngredientComponentStubs.SIMPLE)), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testHashCode(IIngredientMapMutable<ComplexStack, Boolean, Integer> collection) {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientHashMap<>(IngredientComponentStubs.SIMPLE).hashCode())));
        HashMap<IngredientInstanceWrapper<ComplexStack, Integer>, Integer> subMap0 = Maps.newHashMap();
        subMap0.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.COMPLEX, CA01_), 0);
        subMap0.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.COMPLEX, CB02_), 0);
        subMap0.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.COMPLEX, CA91B), 9);
        assertThat(collection.hashCode(), is(new IngredientHashMap<>(IngredientComponentStubs.COMPLEX, subMap0).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientHashMap<>(IngredientComponentStubs.SIMPLE).hashCode())));
    }

}
