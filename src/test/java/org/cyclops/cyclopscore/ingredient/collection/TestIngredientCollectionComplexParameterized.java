package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientCollectionComplexParameterized {

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
                { new IngredientHashSet<>(IngredientComponentStubs.COMPLEX) },
                { new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, 3) },
                { new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()) },
                { new IngredientTreeSet<>(IngredientComponentStubs.COMPLEX) },
                { new IngredientTreeSet<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()) },
                { new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)) },
                { new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(1)) },
                { new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(2)) },
                { new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(3)) },
        });
    }

    @Parameterized.Parameter
    public IIngredientCollectionMutable<ComplexStack, Integer> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.add(CA01_);
        collection.add(CB02_);
        collection.add(CA91B);
    }

    @Test
    public void testContainsMatch() {
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

    @Test
    public void testCount() {
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

    @Test
    public void testIteratorMatch() {
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
