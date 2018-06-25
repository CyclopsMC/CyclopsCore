package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.IngredientMatcherComplex;
import org.cyclops.cyclopscore.ingredient.IngredientSerializerStub;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientCollectionQuantitativeGrouper {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CA02_ = new ComplexStack(ComplexStack.Group.A, 0, 2, null);
    private static final ComplexStack CA05_ = new ComplexStack(ComplexStack.Group.A, 0, 5, null);
    private static final ComplexStack CA0MAX_ = new ComplexStack(ComplexStack.Group.A, 0, Integer.MAX_VALUE, null);
    private static final ComplexStack CA0MAXM_ = new ComplexStack(ComplexStack.Group.A, 0, Integer.MAX_VALUE - 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    private IIngredientCollectionMutable<ComplexStack, Integer> collection;

    @Before
    public void beforeEach() {
        this.collection = new IngredientCollectionQuantitativeGrouper<>(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX));
    }

    @Test
    public void testConstruct() {
        assertThat(new IngredientCollectionQuantitativeGrouper<>(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX)), instanceOf(IngredientCollectionQuantitativeGrouper.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNonEmpty() {
        IngredientArrayList<ComplexStack, Integer> c = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX);
        c.add(CA01_);
        new IngredientCollectionQuantitativeGrouper<>(c);
    }

    @Test
    public void testConstructNoCheck() {
        IngredientArrayList<ComplexStack, Integer> c = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX);
        c.add(CA01_);
        assertThat(new IngredientCollectionQuantitativeGrouper<>(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX), true), instanceOf(IngredientCollectionQuantitativeGrouper.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNoPrimaryQuantifier() {
        IngredientComponent<ComplexStack, Integer> comp =
                new IngredientComponent<>("cyclopscore:complex", new IngredientMatcherComplex(),
                        new IngredientSerializerStub<>(), Lists.newArrayList(
                        new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/group"),
                                ComplexStack.Group.class, true, ComplexStack::getGroup, ComplexStack.Match.GROUP, false),
                        new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/meta"),
                                Integer.class, false, ComplexStack::getMeta, ComplexStack.Match.META, false),
                        new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/tag"),
                                ComplexStack.Tag.class, true, ComplexStack::getTag, ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false)
                )).setUnlocalizedName("recipecomponent.cyclopscore.complex");
        new IngredientCollectionQuantitativeGrouper<>(new IngredientArrayList<>(comp));
    }

    @Test
    public void testAddSingle() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.size(), is(1));
    }

    @Test
    public void testAddMultipleDifferent() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CB02_), is(true));
        assertThat(this.collection.size(), is(2));
    }

    @Test
    public void testAddMultipleEqual() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.size(), is(1));
    }

    @Test
    public void testAddReachMaxQuantity() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA0MAXM_), is(true));
        assertThat(this.collection.size(), is(1));
        assertThat(this.collection.contains(CA0MAX_), is(true));
    }

    @Test
    public void testAddBreachMaxQuantity() {
        assertThat(this.collection.add(CA0MAX_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.size(), is(2));
        assertThat(this.collection.contains(CA0MAX_), is(true));
        assertThat(this.collection.contains(CA01_), is(true));
        assertThat(this.collection.contains(CA05_), is(true));
    }

    @Test
    public void testAddBreachMaxQuantityAlt() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA0MAXM_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.size(), is(2));
        assertThat(this.collection.contains(CA0MAX_), is(true));
        assertThat(this.collection.contains(CA01_), is(true));
        assertThat(this.collection.contains(CA05_), is(true));
    }

    @Test
    public void testAddBreachMaxQuantity2() {
        assertThat(this.collection.add(CA0MAX_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA0MAX_), is(true));
        assertThat(this.collection.size(), is(3));
        assertThat(this.collection.contains(CA0MAX_), is(true));
        assertThat(this.collection.contains(CA01_), is(true));
        assertThat(this.collection.contains(CA05_), is(true));
    }

    @Test
    public void testAddBreachMaxQuantityAlt2() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA0MAXM_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA0MAXM_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.size(), is(3));
        assertThat(this.collection.contains(CA0MAX_), is(true));
        assertThat(this.collection.contains(CA01_), is(true));
        assertThat(this.collection.contains(CA05_), is(true));
    }

    @Test
    public void testAddAllDifferemt() {
        assertThat(this.collection.addAll(Lists.newArrayList(CA01_)), is(true));
        assertThat(this.collection.addAll(Lists.newArrayList(CB02_)), is(true));
        assertThat(this.collection.addAll(Lists.newArrayList(CA91B)), is(true));
        assertThat(this.collection.size(), is(3));
    }

    @Test
    public void testAddAllSame() {
        assertThat(this.collection.addAll(Lists.newArrayList(CA01_)), is(true));
        assertThat(this.collection.addAll(Lists.newArrayList(CA01_)), is(true));
        assertThat(this.collection.addAll(Lists.newArrayList(CA01_)), is(true));
        assertThat(this.collection.addAll(Lists.newArrayList(CA01_)), is(true));
        assertThat(this.collection.addAll(Lists.newArrayList(CA01_)), is(true));
        assertThat(this.collection.size(), is(1));
        assertThat(this.collection.contains(CA05_), is(true));
    }

    @Test
    public void testContainsSingle() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.contains(CA01_), is(true));
        assertThat(this.collection.contains(CA02_), is(false));
        assertThat(this.collection.contains(CB02_), is(false));
    }

    @Test
    public void testContainsMultiple() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.contains(CA01_), is(true));
        assertThat(this.collection.contains(CA02_), is(true));
        assertThat(this.collection.contains(CA05_), is(false));
    }

    @Test
    public void testContainsReachedMaxQuantity() {
        assertThat(this.collection.add(CA0MAX_), is(true));
        assertThat(this.collection.contains(CA01_), is(true));
        assertThat(this.collection.contains(CA0MAX_), is(true));
    }

    @Test
    public void testContainsBreachedMaxQuantity() {
        assertThat(this.collection.add(CA0MAX_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.contains(CA01_), is(true));
        assertThat(this.collection.contains(CA0MAX_), is(true));
    }

    @Test
    public void testContainsAll() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.containsAll(Lists.newArrayList(CA01_, CA02_)), is(true));
        assertThat(this.collection.containsAll(Lists.newArrayList(CA01_, CA05_)), is(false));
    }

    @Test
    public void testRemoveSingle() {
        assertThat(this.collection.remove(CA01_), is(false));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.size(), is(1));
        assertThat(this.collection.remove(CA01_), is(true));
        assertThat(this.collection.size(), is(0));
    }

    @Test
    public void testRemoveMultiple() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.size(), is(1));

        assertThat(this.collection.remove(CA01_), is(true));
        assertThat(this.collection.size(), is(1));

        assertThat(this.collection.remove(CA01_), is(true));
        assertThat(this.collection.size(), is(1));

        assertThat(this.collection.remove(CA01_), is(true));
        assertThat(this.collection.size(), is(0));
    }

    @Test
    public void testRemoveReachedMaxQuantity() {
        assertThat(this.collection.add(CA0MAX_), is(true));

        assertThat(this.collection.remove(CA0MAXM_), is(true));
        assertThat(this.collection.size(), is(1));

        assertThat(this.collection.remove(CA01_), is(true));
        assertThat(this.collection.size(), is(0));
    }

    @Test
    public void testRemoveBreachedMaxQuantity() {
        assertThat(this.collection.add(CA0MAX_), is(true));
        assertThat(this.collection.add(CA01_), is(true));

        assertThat(this.collection.remove(CA0MAX_), is(true));
        assertThat(this.collection.size(), is(1));

        assertThat(this.collection.remove(CA01_), is(true));
        assertThat(this.collection.size(), is(0));
    }

    @Test
    public void testRemoveBreached2MaxQuantity() {
        assertThat(this.collection.add(CA0MAX_), is(true));
        assertThat(this.collection.add(CA0MAX_), is(true));
        assertThat(this.collection.add(CA01_), is(true));

        assertThat(this.collection.remove(CA0MAX_), is(true));
        assertThat(this.collection.size(), is(2));

        assertThat(this.collection.remove(CA0MAX_), is(true));
        assertThat(this.collection.size(), is(1));

        assertThat(this.collection.remove(CA01_), is(true));
        assertThat(this.collection.size(), is(0));
    }

    @Test
    public void testRemoveFailTooLarge() {
        assertThat(this.collection.add(CA01_), is(true));
        assertThat(this.collection.size(), is(1));

        assertThat(this.collection.remove(CA05_), is(false));
        assertThat(this.collection.size(), is(1));
    }

    @Test
    public void testRemoveAllExact() {
        collection.add(CA01_);
        collection.add(CB02_);
        collection.add(CA91B);
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
        collection.add(CA01_);
        collection.add(CB02_);
        collection.add(CA91B);
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
    public void testRemoveAllIterable() {
        collection.add(CA01_);
        collection.add(CB02_);
        collection.add(CA91B);
        Assert.assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(CA01_, CB02_, CA91B, CA01B)), is(3));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllIterableMatchAny() {
        collection.add(CA01_);
        collection.add(CB02_);
        collection.add(CA91B);
        Assert.assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(CA01_, CB02_), ComplexStack.Match.ANY), is(3));
        Assert.assertThat(collection.size(), is(0));
    }

    @Test
    public void testRemoveAllIterableMatchExact() {
        collection.add(CA01_);
        collection.add(CB02_);
        collection.add(CA91B);
        Assert.assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(CA01_, CB02_), ComplexStack.Match.EXACT), is(2));
        Assert.assertThat(collection.size(), is(1));
    }

}
