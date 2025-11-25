package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientCollectionSetComplex {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    public Stream<Arguments> data() {
        return Stream.of(
                new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, 3),
                new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()),
                new IngredientTreeSet<>(IngredientComponentStubs.COMPLEX),
                new IngredientTreeSet<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList())
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
    public void testEquals(IngredientSet<ComplexStack, Integer> collection) {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals("abc"), is(false));
        assertThat(collection.equals(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(null), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(CA01_, CB02_, CA91B))), is(true));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2))), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testHashCode(IngredientSet<ComplexStack, Integer> collection) {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, CA01_, CB02_, CA91B).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE).hashCode())));
    }

}
