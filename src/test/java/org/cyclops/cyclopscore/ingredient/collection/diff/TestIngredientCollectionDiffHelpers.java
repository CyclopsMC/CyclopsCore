package org.cyclops.cyclopscore.ingredient.collection.diff;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionPrototypeMap;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientCollectionDiffHelpers {

    private static final IngredientComponent<ComplexStack, Integer> COMP = IngredientComponentStubs.COMPLEX;

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CA02_ = new ComplexStack(ComplexStack.Group.A, 0, 2, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA92B = new ComplexStack(ComplexStack.Group.A, 9, 2, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    private IngredientCollectionPrototypeMap<ComplexStack, Integer> newInstancesCache;

    @Before
    public void beforeEach() {
        newInstancesCache = new IngredientCollectionPrototypeMap<>(COMP, true);
    }

    @Test
    public void testGetDiffEmpty() {
        IngredientCollectionDiff<ComplexStack, Integer> diff = IngredientCollectionDiffHelpers.getDiff(
                COMP, null, newInstancesCache, Iterators.empty());

        assertThat(Sets.newHashSet(newInstancesCache), is(Sets.newHashSet()));
        assertThat(Sets.newHashSet(diff.getAdditions()), is(Sets.newHashSet()));
        assertThat(Sets.newHashSet(diff.getDeletions()), is(Sets.newHashSet()));
        assertThat(diff.isCompletelyEmpty(), is(true));
    }

    @Test
    public void testGetDiffEmptyToNonEmpty() {
        IngredientCollectionDiff<ComplexStack, Integer> diff = IngredientCollectionDiffHelpers.getDiff(
                COMP, null, newInstancesCache, Lists.newArrayList(CA01_, CB02_, CA01_).iterator());

        assertThat(Sets.newHashSet(newInstancesCache), is(Sets.newHashSet(CA02_, CB02_)));
        assertThat(Sets.newHashSet(diff.getAdditions()), is(Sets.newHashSet(CA02_, CB02_)));
        assertThat(Sets.newHashSet(diff.getDeletions()), is(Sets.newHashSet()));
        assertThat(diff.isCompletelyEmpty(), is(false));
    }

    @Test
    public void testGetDiffNonEmptyToEmpty() {
        IngredientCollectionPrototypeMap<ComplexStack, Integer> oldInstancesCache = new IngredientCollectionPrototypeMap<>(COMP, true);
        oldInstancesCache.add(CA02_);
        oldInstancesCache.add(CB02_);

        IngredientCollectionDiff<ComplexStack, Integer> diff = IngredientCollectionDiffHelpers.getDiff(
                COMP, oldInstancesCache, newInstancesCache, Iterators.empty());

        assertThat(Sets.newHashSet(newInstancesCache), is(Sets.newHashSet()));
        assertThat(Sets.newHashSet(diff.getAdditions()), is(Sets.newHashSet()));
        assertThat(Sets.newHashSet(diff.getDeletions()), is(Sets.newHashSet(CA02_, CB02_)));
        assertThat(diff.isCompletelyEmpty(), is(true));
    }

    @Test
    public void testGetDiffNonEmptyToNonEmpty() {
        IngredientCollectionPrototypeMap<ComplexStack, Integer> oldInstancesCache = new IngredientCollectionPrototypeMap<>(COMP, true);
        oldInstancesCache.add(CA02_);
        oldInstancesCache.add(CB02_);

        IngredientCollectionDiff<ComplexStack, Integer> diff = IngredientCollectionDiffHelpers.getDiff(
                COMP, oldInstancesCache, newInstancesCache, Lists.newArrayList(CA91B, CA01_, CA91B).iterator());

        assertThat(Sets.newHashSet(newInstancesCache), is(Sets.newHashSet(CA01_, CA92B)));
        assertThat(Sets.newHashSet(diff.getAdditions()), is(Sets.newHashSet(CA92B)));
        assertThat(Sets.newHashSet(diff.getDeletions()), is(Sets.newHashSet(CA01_, CB02_)));
        assertThat(diff.isCompletelyEmpty(), is(false));
    }

    @Test
    public void testApplyDiffNullNullCollection() {
        IngredientCollectionDiff<ComplexStack, Integer> diff = new IngredientCollectionDiff<>(
                null, null, false);
        IngredientArrayList<ComplexStack, Integer> collection = new IngredientArrayList<>(COMP);

        IngredientCollectionDiffHelpers.applyDiff(COMP, diff, collection);

        assertThat(Sets.newHashSet(collection), is(Sets.newHashSet()));
    }

    @Test
    public void testApplyDiffEmptyEmptyCollection() {
        IngredientCollectionDiff<ComplexStack, Integer> diff = new IngredientCollectionDiff<>(
                new IngredientArrayList<>(COMP), new IngredientArrayList<>(COMP), false);
        IngredientArrayList<ComplexStack, Integer> collection = new IngredientArrayList<>(COMP);

        IngredientCollectionDiffHelpers.applyDiff(COMP, diff, collection);

        assertThat(Sets.newHashSet(collection), is(Sets.newHashSet()));
    }

    @Test
    public void testApplyDiffCollection() {
        IngredientArrayList<ComplexStack, Integer> additions = new IngredientArrayList<>(COMP, CA02_, CA91B);
        IngredientArrayList<ComplexStack, Integer> deletions = new IngredientArrayList<>(COMP, CA01B);
        IngredientCollectionDiff<ComplexStack, Integer> diff = new IngredientCollectionDiff<>(
                additions, deletions, false);
        IngredientArrayList<ComplexStack, Integer> collection = new IngredientArrayList<>(COMP, CA01B, CA91B);

        IngredientCollectionDiffHelpers.applyDiff(COMP, diff, collection);

        assertThat(Sets.newHashSet(collection), is(Sets.newHashSet(CA02_, CA92B)));
    }

    @Test
    public void testApplyDiffNullNullPrototyped() {
        IngredientCollectionDiff<ComplexStack, Integer> diff = new IngredientCollectionDiff<>(
                null, null, false);
        IngredientCollectionPrototypeMap<ComplexStack, Integer> collection = new IngredientCollectionPrototypeMap<>(COMP);

        IngredientCollectionDiffHelpers.applyDiff(COMP, diff, collection);

        assertThat(Sets.newHashSet(collection), is(Sets.newHashSet()));
    }

    @Test
    public void testApplyDiffEmptyEmptyPrototyped() {
        IngredientCollectionDiff<ComplexStack, Integer> diff = new IngredientCollectionDiff<>(
                new IngredientArrayList<>(COMP), new IngredientArrayList<>(COMP), false);
        IngredientCollectionPrototypeMap<ComplexStack, Integer> collection = new IngredientCollectionPrototypeMap<>(COMP);

        IngredientCollectionDiffHelpers.applyDiff(COMP, diff, collection);

        assertThat(Sets.newHashSet(collection), is(Sets.newHashSet()));
    }

    @Test
    public void testApplyDiffPrototyped() {
        IngredientArrayList<ComplexStack, Integer> additions = new IngredientArrayList<>(COMP, CA02_, CA91B);
        IngredientArrayList<ComplexStack, Integer> deletions = new IngredientArrayList<>(COMP, CA01B);
        IngredientCollectionDiff<ComplexStack, Integer> diff = new IngredientCollectionDiff<>(
                additions, deletions, false);
        IngredientCollectionPrototypeMap<ComplexStack, Integer> collection = new IngredientCollectionPrototypeMap<>(COMP);
        collection.add(CA01B);
        collection.add(CA91B);

        IngredientCollectionDiffHelpers.applyDiff(COMP, diff, collection);

        assertThat(Sets.newHashSet(collection), is(Sets.newHashSet(CA02_, CA92B)));
    }

}
