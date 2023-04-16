package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientCollectionSingleClassifiedCollapsedComplex {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CA0M1_ = new ComplexStack(ComplexStack.Group.A, 0, -1, null);
    private static final ComplexStack CA02_ = new ComplexStack(ComplexStack.Group.A, 0, 2, null);
    private static final ComplexStack CA03_ = new ComplexStack(ComplexStack.Group.A, 0, 3, null);
    private static final ComplexStack CA04_ = new ComplexStack(ComplexStack.Group.A, 0, 4, null);
    private static final ComplexStack CA05_ = new ComplexStack(ComplexStack.Group.A, 0, 5, null);
    private static final ComplexStack CA0M5_ = new ComplexStack(ComplexStack.Group.A, 0, -5, null);

    private static final ComplexStack CB01_ = new ComplexStack(ComplexStack.Group.B, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CB03_ = new ComplexStack(ComplexStack.Group.B, 0, 3, null);
    private static final ComplexStack CB04_ = new ComplexStack(ComplexStack.Group.B, 0, 4, null);
    private static final ComplexStack CB05_ = new ComplexStack(ComplexStack.Group.B, 0, 5, null);

    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA92B = new ComplexStack(ComplexStack.Group.A, 9, 2, ComplexStack.Tag.B);
    private static final ComplexStack CA95B = new ComplexStack(ComplexStack.Group.A, 9, 5, ComplexStack.Tag.B);

    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA02B = new ComplexStack(ComplexStack.Group.A, 0, 2, ComplexStack.Tag.B);
    private static final ComplexStack CA05B = new ComplexStack(ComplexStack.Group.A, 0, 5, ComplexStack.Tag.B);

    private IngredientCollectionSingleClassifiedCollapsed<ComplexStack, Integer, ?> collection;

    @Before
    public void beforeEach() {
        collection = new IngredientCollectionSingleClassifiedCollapsed<>(
                IngredientComponentStubs.COMPLEX,
                () -> new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0));
    }

    @Test
    public void addSame() {
        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));

        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.contains(CA01_), is(true));
        assertThat(collection.contains(CA02_), is(false));
        assertThat(collection.contains(CA05_), is(false));
        assertThat(collection.size(), is(1));

        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.contains(CA01_), is(false));
        assertThat(collection.contains(CA02_), is(true));
        assertThat(collection.contains(CA05_), is(false));
        assertThat(collection.size(), is(1));

        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.contains(CA01_), is(false));
        assertThat(collection.contains(CA02_), is(false));
        assertThat(collection.contains(CA05_), is(true));
        assertThat(collection.size(), is(1));
    }

    @Test
    public void removeSame() {
        assertThat(collection.add(CA05_), is(true));
        assertThat(collection.contains(CA01_), is(false));
        assertThat(collection.contains(CA02_), is(false));
        assertThat(collection.contains(CA05_), is(true));
        assertThat(collection.size(), is(1));

        assertThat(collection.remove(CA01_), is(true));
        assertThat(collection.remove(CA01_), is(true));
        assertThat(collection.remove(CA01_), is(true));
        assertThat(collection.contains(CA01_), is(false));
        assertThat(collection.contains(CA02_), is(true));
        assertThat(collection.contains(CA05_), is(false));
        assertThat(collection.size(), is(1));

        assertThat(collection.remove(CA01_), is(true));
        assertThat(collection.contains(CA01_), is(true));
        assertThat(collection.contains(CA02_), is(false));
        assertThat(collection.contains(CA05_), is(false));
        assertThat(collection.size(), is(1));

        assertThat(collection.remove(CA01_), is(true));
        assertThat(collection.contains(CA01_), is(false));
        assertThat(collection.contains(CA02_), is(false));
        assertThat(collection.contains(CA05_), is(false));
        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));

        assertThat(collection.remove(CA01_), is(false));
    }

    @Test
    public void contains() {
        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.add(CA01_), is(true));

        assertThat(collection.contains(CA01_), is(false));
        assertThat(collection.contains(CA02_), is(false));
        assertThat(collection.contains(CA03_), is(true));
        assertThat(collection.contains(CA04_), is(false));
    }

    @Test
    public void containsMatch() {
        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.add(CA01_), is(true));

        assertThat(collection.contains(CB01_, ComplexStack.Match.META), is(true));
        assertThat(collection.contains(CB02_, ComplexStack.Match.META), is(true));
        assertThat(collection.contains(CB03_, ComplexStack.Match.META), is(true));
        assertThat(collection.contains(CB04_, ComplexStack.Match.META), is(false));

        assertThat(collection.contains(CB01_, ComplexStack.Match.GROUP), is(false));
        assertThat(collection.contains(CB02_, ComplexStack.Match.GROUP), is(false));
        assertThat(collection.contains(CB03_, ComplexStack.Match.GROUP), is(false));
        assertThat(collection.contains(CB04_, ComplexStack.Match.GROUP), is(false));

        assertThat(collection.contains(CB01_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(false));
        assertThat(collection.contains(CB02_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(false));
        assertThat(collection.contains(CB03_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(true));
        assertThat(collection.contains(CB04_, ComplexStack.Match.META | ComplexStack.Match.AMOUNT), is(false));

        assertThat(collection.contains(CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(false));
        assertThat(collection.contains(CB02_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(false));
        assertThat(collection.contains(CB03_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(false));
        assertThat(collection.contains(CB04_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT), is(false));
    }

    @Test
    public void clear() {
        assertThat(collection.add(CA01_), is(true));

        assertThat(collection.contains(CA01_), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.isEmpty(), is(false));

        collection.clear();

        assertThat(collection.contains(CA01_), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.isEmpty(), is(true));
    }

    @Test
    public void setGetQuantity() {
        assertThat(collection.getQuantity(CA01_), is(0L));
        assertThat(collection.size(), is(0));
        assertThat(collection.isEmpty(), is(true));

        collection.setQuantity(CA01_, 10);

        assertThat(collection.getQuantity(CA01_), is(10L));
        assertThat(collection.size(), is(1));
        assertThat(collection.isEmpty(), is(false));

        collection.setQuantity(CA01_, 20);

        assertThat(collection.getQuantity(CA05_), is(20L));
        assertThat(collection.size(), is(1));
        assertThat(collection.isEmpty(), is(false));

        collection.setQuantity(CA01_, 0);

        assertThat(collection.getQuantity(CA05_), is(0L));
        assertThat(collection.size(), is(0));
        assertThat(collection.isEmpty(), is(true));
    }

    @Test
    public void iterator() {
        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.add(CB02_), is(true));
        assertThat(collection.add(CA02_), is(true));
        assertThat(collection.add(CA92B), is(true));
        assertThat(collection.add(CA05B), is(true));
        assertThat(collection.add(CA02_), is(true));

        IngredientHashSet<ComplexStack, Integer> available = new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, collection.iterator());
        assertThat(available, is(new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(CA05_, CB02_, CA92B, CA05B))));
    }

    @Test
    public void iteratorMatch() {
        assertThat(collection.add(CA01_), is(true));
        assertThat(collection.add(CB02_), is(true));
        assertThat(collection.add(CA02_), is(true));
        assertThat(collection.add(CA92B), is(true));
        assertThat(collection.add(CA05B), is(true));
        assertThat(collection.add(CA02_), is(true));

        IngredientHashSet<ComplexStack, Integer> available1 = new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, collection.iterator(CA05_, ComplexStack.Match.EXACT));
        assertThat(available1, is(new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(CA05_))));

        IngredientHashSet<ComplexStack, Integer> available2 = new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, collection.iterator(CA05_, ComplexStack.Match.ANY));
        assertThat(available2, is(new IngredientHashSet<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(CA05_, CB02_, CA92B, CA05B))));
    }

}