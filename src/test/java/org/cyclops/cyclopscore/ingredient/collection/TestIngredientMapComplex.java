package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientMapComplex {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new IngredientHashMap<>(IngredientComponentStubs.COMPLEX) },
                { new IngredientHashMap<>(IngredientComponentStubs.COMPLEX, 3) },
                { new IngredientHashMap<>(IngredientComponentStubs.COMPLEX, new IngredientHashMap<>(IngredientComponentStubs.COMPLEX)) },
                { new IngredientHashMap<>(IngredientComponentStubs.COMPLEX, Maps.newHashMap()) },
                { new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX) },
                { new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX, new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX)) },
                { new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX, Maps.newTreeMap()) },
        });
    }

    @Parameterized.Parameter
    public IIngredientMapMutable<ComplexStack, Boolean, Integer> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.put(CA01_, 0);
        collection.put(CB02_, 0);
        collection.put(CA91B, 9);
    }

    @Test
    public void testEquals() {
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

    @Test
    public void testHashCode() {
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
