package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
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
public class TestIngredientCollectionListSimple {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 3) },
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()) },
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, new Integer[0]) },
                { new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()) },
        });
    }

    @Parameterized.Parameter
    public IngredientList<Integer, Boolean> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.add(0);
        collection.add(1);
        collection.add(2);
    }

    @Test
    public void testEquals() {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals("abc"), is(false));
        assertThat(collection.equals(new IngredientCollectionEmpty<>(IngredientComponentStubs.COMPLEX)), is(false));
        assertThat(collection.equals(null), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1, 2)), is(true));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1, 3)), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1)), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX)), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2))), is(false));
    }

    @Test
    public void testHashCode() {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.COMPLEX).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1, 2).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX).hashCode())));
    }

}
