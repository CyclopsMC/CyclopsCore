package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientCollectionListComplex {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new IngredientArrayList<>(IngredientComponentStubs.COMPLEX) },
                { new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, 3) },
                { new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()) },
                { new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, new ComplexStack[0]) },
                { new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX) },
                { new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()) },
        });
    }

    @Parameterized.Parameter
    public IngredientList<ComplexStack, Integer> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.add(CA01_);
        collection.add(CB02_);
        collection.add(CA91B);
    }

    @Test
    public void testEquals() {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals("abc"), is(false));
        assertThat(collection.equals(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(null), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, CA01_, CB02_, CA91B)), is(true));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2))), is(false));
    }

    @Test
    public void testHashCode() {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, CA01_, CB02_, CA91B).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE).hashCode())));
    }

}
