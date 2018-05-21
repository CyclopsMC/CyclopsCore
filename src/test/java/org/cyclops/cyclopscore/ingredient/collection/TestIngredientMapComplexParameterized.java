package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientMapComplexParameterized {

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
                { new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)) },
                { new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(1)) },
                { new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(2)) },
                { new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(3)) },
        });
    }

    @Parameterized.Parameter
    public IIngredientMapMutable<ComplexStack, Integer, Integer> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.put(CA01_, 0);
        collection.put(CB02_, 1);
        collection.put(CA91B, 9);
    }

    @Test
    public void testContainsMatch() {
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

    @Test
    public void testCount() {
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

    @Test
    public void testIteratorMatch() {
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

    @Test
    public void testGetAll() {
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

    @Test
    public void testKeySetMatch() {
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

    @Test
    public void testRemoveAllExact() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.EXACT), is(0));
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.EXACT), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.EXACT), is(1));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.EXACT), is(1));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllAny() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.ANY), is(3));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.ANY), is(0));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.ANY), is(0));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.ANY), is(0));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllGroup() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP), is(2));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP), is(0));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP), is(1));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP), is(0));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllMeta() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.META), is(2));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.META), is(1));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.META), is(0));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.META), is(0));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllAmount() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.AMOUNT), is(2));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.AMOUNT), is(0));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.AMOUNT), is(1));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.AMOUNT), is(0));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllTag() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.TAG), is(0));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.TAG), is(2));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.TAG), is(0));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllGroupMeta() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(1));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META), is(0));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllGroupAmount() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(2));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(0));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(1));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(0));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllGroupTag() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(0));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllMetaAmount() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(0));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllMetaTag() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(0));
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(2));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.META | ComplexStack.Match.TAG), is(0));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllAmountTag() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(0));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllGroupMetaAmount() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(1));
        Assert.assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(0));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllGroupMetaTag() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(0));
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllGroupAmountTag() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(CA01B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CA91B, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(0));
        Assert.assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT | ComplexStack.Match.TAG), is(1));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllIterable() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(CA01_, CB02_, CA91B, CA01B), ComplexStack.Match.EXACT), is(3));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllIterableAy() {
        Assert.assertThat(collection.size(), is(3));
        assertThat(collection.removeAll(Lists.newArrayList(CA01_, CB02_), ComplexStack.Match.ANY), is(3));
        Assert.assertThat(collection.size(), is(0));
    }

}
