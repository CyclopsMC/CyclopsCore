package org.cyclops.cyclopscore.ingredient.collection.diff;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientCollectionDiffManager {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CA02_ = new ComplexStack(ComplexStack.Group.A, 0, 2, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA92B = new ComplexStack(ComplexStack.Group.A, 9, 2, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    @Test
    public void testOnChangeEmpty() {
        IngredientCollectionDiffManager<ComplexStack, Integer> manager = new IngredientCollectionDiffManager<>(IngredientComponentStubs.COMPLEX);
        IngredientCollectionDiff<ComplexStack, Integer> diff = manager.onChange(Iterators.empty());

        assertThat(Sets.newHashSet(diff.getAdditions()), is(Sets.newHashSet()));
        assertThat(Sets.newHashSet(diff.getDeletions()), is(Sets.newHashSet()));
        assertThat(diff.isCompletelyEmpty(), is(true));
    }

    @Test
    public void testOnChangeNonEmpty() {
        IngredientCollectionDiffManager<ComplexStack, Integer> manager = new IngredientCollectionDiffManager<>(IngredientComponentStubs.COMPLEX);
        IngredientCollectionDiff<ComplexStack, Integer> diff = manager.onChange(Lists.newArrayList(CA01_).iterator());

        assertThat(Sets.newHashSet(diff.getAdditions()), is(Sets.newHashSet(CA01_)));
        assertThat(Sets.newHashSet(diff.getDeletions()), is(Sets.newHashSet()));
        assertThat(diff.isCompletelyEmpty(), is(false));
    }

    @Test
    public void testOnChangeIterations() {
        IngredientCollectionDiffManager<ComplexStack, Integer> manager = new IngredientCollectionDiffManager<>(IngredientComponentStubs.COMPLEX);
        IngredientCollectionDiff<ComplexStack, Integer> diff1 = manager.onChange(Lists.newArrayList(CA02_).iterator());

        assertThat(Sets.newHashSet(diff1.getAdditions()), is(Sets.newHashSet(CA02_)));
        assertThat(Sets.newHashSet(diff1.getDeletions()), is(Sets.newHashSet()));
        assertThat(diff1.isCompletelyEmpty(), is(false));

        IngredientCollectionDiff<ComplexStack, Integer> diff2 = manager.onChange(Lists.newArrayList(CA01_).iterator());

        assertThat(Sets.newHashSet(diff2.getAdditions()), is(Sets.newHashSet()));
        assertThat(Sets.newHashSet(diff2.getDeletions()), is(Sets.newHashSet(CA01_)));
        assertThat(diff2.isCompletelyEmpty(), is(false));

        IngredientCollectionDiff<ComplexStack, Integer> diff3 = manager.onChange(Lists.newArrayList(CA01_, CA91B).iterator());

        assertThat(Sets.newHashSet(diff3.getAdditions()), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(diff3.getDeletions()), is(Sets.newHashSet()));
        assertThat(diff3.isCompletelyEmpty(), is(false));

        IngredientCollectionDiff<ComplexStack, Integer> diff4 = manager.onChange(Lists.newArrayList(CA92B).iterator());

        assertThat(Sets.newHashSet(diff4.getAdditions()), is(Sets.newHashSet(CA91B)));
        assertThat(Sets.newHashSet(diff4.getDeletions()), is(Sets.newHashSet(CA01_)));
        assertThat(diff4.isCompletelyEmpty(), is(false));

        IngredientCollectionDiff<ComplexStack, Integer> diff5 = manager.onChange(Iterators.empty());

        assertThat(Sets.newHashSet(diff5.getAdditions()), is(Sets.newHashSet()));
        assertThat(Sets.newHashSet(diff5.getDeletions()), is(Sets.newHashSet(CA92B)));
        assertThat(diff5.isCompletelyEmpty(), is(true));
    }

}
