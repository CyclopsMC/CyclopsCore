package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestFilteredIngredientIterator {

    private static final ComplexStack C0 = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack C1 = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack C2 = new ComplexStack(ComplexStack.Group.A, 10, 1, ComplexStack.Tag.B);
    private static final ComplexStack C3 = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    @Test
    public void testEmpty() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Collections.emptyList(), IngredientComponentStubs.COMPLEX.getMatcher(), C0, ComplexStack.Match.ANY);
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyNext() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Collections.emptyList(), IngredientComponentStubs.COMPLEX.getMatcher(), C0, ComplexStack.Match.ANY);
        it.next();
    }

    @Test
    public void testNonEmptyAny() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Lists.newArrayList(C0, C1, C2), IngredientComponentStubs.COMPLEX.getMatcher(), C0, ComplexStack.Match.ANY);
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(C0));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(C1));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(C2));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyAnyNextAtEnd() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Lists.newArrayList(C0, C1, C2), IngredientComponentStubs.COMPLEX.getMatcher(), C0, ComplexStack.Match.ANY);
        assertThat(it.next(), is(C0));
        assertThat(it.next(), is(C1));
        assertThat(it.next(), is(C2));
        it.next();
    }

    @Test
    public void testNonEmptyExact() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Lists.newArrayList(C0, C1, C2), IngredientComponentStubs.COMPLEX.getMatcher(), C0, ComplexStack.Match.EXACT);
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(C0));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyExactNextAtEnd() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Lists.newArrayList(C0, C1, C2), IngredientComponentStubs.COMPLEX.getMatcher(), C0, ComplexStack.Match.EXACT);
        assertThat(it.next(), is(C0));
        it.next();
    }

    @Test
    public void testNonEmptyMeta1() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Lists.newArrayList(C0, C1, C2), IngredientComponentStubs.COMPLEX.getMatcher(), C3, ComplexStack.Match.META);
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(C0));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(C1));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyMeta1NextAtEnd() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Lists.newArrayList(C0, C1, C2), IngredientComponentStubs.COMPLEX.getMatcher(), C3, ComplexStack.Match.META);
        assertThat(it.next(), is(C0));
        assertThat(it.next(), is(C1));
        it.next();
    }

    @Test
    public void testNonEmptyMeta2() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Lists.newArrayList(C0, C1, C2), IngredientComponentStubs.COMPLEX.getMatcher(), C1, ComplexStack.Match.META);
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(C0));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(C1));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNonEmptyMeta2NextAtEnd() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Lists.newArrayList(C0, C1, C2), IngredientComponentStubs.COMPLEX.getMatcher(), C1, ComplexStack.Match.META);
        assertThat(it.next(), is(C0));
        assertThat(it.next(), is(C1));
        it.next();
    }

    @Test
    public void testRemove() {
        Iterable<ComplexStack> in = Lists.newArrayList(C0, C1, C2);
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                in, IngredientComponentStubs.COMPLEX.getMatcher(), C1, ComplexStack.Match.META);
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(C0));
        it.remove();
        assertThat(in, equalTo(Lists.newArrayList(C1, C2)));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(C1));
        it.remove();
        assertThat(in, equalTo(Lists.newArrayList(C2)));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveMultiple() {
        FilteredIngredientIterator<ComplexStack, Integer> it = new FilteredIngredientIterator<>(
                Lists.newArrayList(C0, C1, C2), IngredientComponentStubs.COMPLEX.getMatcher(), C1, ComplexStack.Match.META);
        it.next();
        it.remove();
        it.remove();
    }

}
