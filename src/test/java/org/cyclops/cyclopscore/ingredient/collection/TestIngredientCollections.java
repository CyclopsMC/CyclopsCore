package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientCollections {

    @Test
    public void testPrivateConstructor() {
        new IngredientCollections();
    }

    @Test
    public void testEmptyCollection() {
        assertThat(IngredientCollections.emptyCollection(IngredientComponentStubs.SIMPLE),
                instanceOf(IngredientCollectionEmpty.class));
        assertThat(IngredientCollections.emptyCollection(IngredientComponentStubs.SIMPLE).getComponent(),
                is(IngredientComponentStubs.SIMPLE));
    }

    @Test
    public void testEquals() {
        IIngredientCollection<Integer, Boolean> empty1 = IngredientCollections.emptyCollection(IngredientComponentStubs.SIMPLE);
        IIngredientCollection<Integer, Boolean> empty2 = IngredientCollections.emptyCollection(IngredientComponentStubs.SIMPLE);
        IIngredientCollection<ComplexStack, Integer> empty1c = IngredientCollections.emptyCollection(IngredientComponentStubs.COMPLEX);
        IngredientArrayList<Integer, Boolean> cols1 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        IngredientArrayList<Integer, Boolean> cols2 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        IngredientArrayList<Integer, Boolean> cols3 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1));
        IngredientArrayList<Integer, Boolean> cols4 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 3));
        IngredientArrayList<ComplexStack, Integer> colc1 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 2, 0, ComplexStack.Tag.A)
        ));
        IngredientArrayList<ComplexStack, Integer> colc2 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 2, 0, ComplexStack.Tag.A)
        ));
        IngredientArrayList<ComplexStack, Integer> colc3 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A)
        ));
        IngredientArrayList<ComplexStack, Integer> colc4 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 3, 0, ComplexStack.Tag.A)
        ));

        assertThat(IngredientCollections.equalsOrdered(empty1, empty1), is(true));
        assertThat(IngredientCollections.equalsOrdered(empty1, empty2), is(true));
        assertThat(IngredientCollections.equalsOrdered(empty1, empty1c), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1, cols1), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1, cols2), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1, cols3), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1, cols4), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1, colc1), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1, colc2), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1, colc3), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1, colc4), is(false));

        assertThat(IngredientCollections.equalsOrdered(empty2, empty1), is(true));
        assertThat(IngredientCollections.equalsOrdered(empty2, empty2), is(true));
        assertThat(IngredientCollections.equalsOrdered(empty2, empty1c), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty2, cols1), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty2, cols2), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty2, cols3), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty2, cols4), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty2, colc1), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty2, colc2), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty2, colc3), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty2, colc4), is(false));

        assertThat(IngredientCollections.equalsOrdered(empty1c, empty1), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1c, empty2), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1c, empty1c), is(true));
        assertThat(IngredientCollections.equalsOrdered(empty1c, cols1), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1c, cols2), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1c, cols3), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1c, cols4), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1c, colc1), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1c, colc2), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1c, colc3), is(false));
        assertThat(IngredientCollections.equalsOrdered(empty1c, colc4), is(false));

        assertThat(IngredientCollections.equalsOrdered(cols1, empty1), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols1, empty2), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols1, empty1c), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols1, cols1), is(true));
        assertThat(IngredientCollections.equalsOrdered(cols1, cols2), is(true));
        assertThat(IngredientCollections.equalsOrdered(cols1, cols3), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols1, cols4), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols1, colc1), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols1, colc2), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols1, colc3), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols1, colc4), is(false));

        assertThat(IngredientCollections.equalsOrdered(cols2, empty1), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols2, empty2), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols2, empty1c), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols2, cols1), is(true));
        assertThat(IngredientCollections.equalsOrdered(cols2, cols2), is(true));
        assertThat(IngredientCollections.equalsOrdered(cols2, cols3), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols2, cols4), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols2, colc1), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols2, colc2), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols2, colc3), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols2, colc4), is(false));

        assertThat(IngredientCollections.equalsOrdered(cols3, empty1), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols3, empty2), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols3, empty1c), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols3, cols1), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols3, cols2), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols3, cols3), is(true));
        assertThat(IngredientCollections.equalsOrdered(cols3, cols4), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols3, colc1), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols3, colc2), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols3, colc3), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols3, colc4), is(false));

        assertThat(IngredientCollections.equalsOrdered(cols4, empty1), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols4, empty2), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols4, empty1c), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols4, cols1), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols4, cols2), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols4, cols3), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols4, cols4), is(true));
        assertThat(IngredientCollections.equalsOrdered(cols4, colc1), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols4, colc2), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols4, colc3), is(false));
        assertThat(IngredientCollections.equalsOrdered(cols4, colc4), is(false));

        assertThat(IngredientCollections.equalsOrdered(colc1, empty1), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc1, empty2), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc1, empty1c), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc1, cols1), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc1, cols2), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc1, cols3), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc1, cols4), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc1, colc1), is(true));
        assertThat(IngredientCollections.equalsOrdered(colc1, colc2), is(true));
        assertThat(IngredientCollections.equalsOrdered(colc1, colc3), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc1, colc4), is(false));

        assertThat(IngredientCollections.equalsOrdered(colc2, empty1), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc2, empty2), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc2, empty1c), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc2, cols1), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc2, cols2), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc2, cols3), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc2, cols4), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc2, colc1), is(true));
        assertThat(IngredientCollections.equalsOrdered(colc2, colc2), is(true));
        assertThat(IngredientCollections.equalsOrdered(colc2, colc3), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc2, colc4), is(false));

        assertThat(IngredientCollections.equalsOrdered(colc3, empty1), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc3, empty2), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc3, empty1c), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc3, cols1), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc3, cols2), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc3, cols3), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc3, cols4), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc3, colc1), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc3, colc2), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc3, colc3), is(true));
        assertThat(IngredientCollections.equalsOrdered(colc3, colc4), is(false));

        assertThat(IngredientCollections.equalsOrdered(colc4, empty1), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc4, empty2), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc4, empty1c), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc4, cols1), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc4, cols2), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc4, cols3), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc4, cols4), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc4, colc1), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc4, colc2), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc4, colc3), is(false));
        assertThat(IngredientCollections.equalsOrdered(colc4, colc4), is(true));
    }

    @Test
    public void testEqualsChecked() {
        IIngredientCollection<Integer, Boolean> empty1 = IngredientCollections.emptyCollection(IngredientComponentStubs.SIMPLE);
        IIngredientCollection<Integer, Boolean> empty2 = IngredientCollections.emptyCollection(IngredientComponentStubs.SIMPLE);
        IIngredientCollection<ComplexStack, Integer> empty1c = IngredientCollections.emptyCollection(IngredientComponentStubs.COMPLEX);
        IngredientArrayList<Integer, Boolean> cols1 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        IngredientArrayList<Integer, Boolean> cols2 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        IngredientArrayList<Integer, Boolean> cols3 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1));
        IngredientArrayList<Integer, Boolean> cols4 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 3));
        IngredientArrayList<ComplexStack, Integer> colc1 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 2, 0, ComplexStack.Tag.A)
        ));
        IngredientArrayList<ComplexStack, Integer> colc2 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 2, 0, ComplexStack.Tag.A)
        ));
        IngredientArrayList<ComplexStack, Integer> colc3 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A)
        ));
        IngredientArrayList<ComplexStack, Integer> colc4 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 3, 0, ComplexStack.Tag.A)
        ));

        assertThat(IngredientCollections.equalsCheckedOrdered(empty1, empty1), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty1, empty2), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty1, cols1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty1, cols2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty1, cols3), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty1, cols4), is(false));

        assertThat(IngredientCollections.equalsCheckedOrdered(empty2, empty1), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty2, empty2), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty2, cols1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty2, cols2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty2, cols3), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty2, cols4), is(false));

        assertThat(IngredientCollections.equalsCheckedOrdered(empty1c, empty1c), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty1c, colc1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty1c, colc2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty1c, colc3), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(empty1c, colc4), is(false));

        assertThat(IngredientCollections.equalsCheckedOrdered(cols1, empty1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols1, empty2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols1, cols1), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols1, cols2), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols1, cols3), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols1, cols4), is(false));

        assertThat(IngredientCollections.equalsCheckedOrdered(cols2, empty1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols2, empty2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols2, cols1), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols2, cols2), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols2, cols3), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols2, cols4), is(false));

        assertThat(IngredientCollections.equalsCheckedOrdered(cols3, empty1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols3, empty2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols3, cols1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols3, cols2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols3, cols3), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols3, cols4), is(false));

        assertThat(IngredientCollections.equalsCheckedOrdered(cols4, empty1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols4, empty2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols4, cols1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols4, cols2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols4, cols3), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(cols4, cols4), is(true));

        assertThat(IngredientCollections.equalsCheckedOrdered(colc1, empty1c), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc1, colc1), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc1, colc2), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc1, colc3), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc1, colc4), is(false));

        assertThat(IngredientCollections.equalsCheckedOrdered(colc2, empty1c), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc2, colc1), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc2, colc2), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc2, colc3), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc2, colc4), is(false));

        assertThat(IngredientCollections.equalsCheckedOrdered(colc3, empty1c), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc3, colc1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc3, colc2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc3, colc3), is(true));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc3, colc4), is(false));

        assertThat(IngredientCollections.equalsCheckedOrdered(colc4, empty1c), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc4, colc1), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc4, colc2), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc4, colc3), is(false));
        assertThat(IngredientCollections.equalsCheckedOrdered(colc4, colc4), is(true));
    }

    @Test
    public void testHash() {
        IIngredientCollection<Integer, Boolean> empty1 = IngredientCollections.emptyCollection(IngredientComponentStubs.SIMPLE);
        IIngredientCollection<Integer, Boolean> empty2 = IngredientCollections.emptyCollection(IngredientComponentStubs.SIMPLE);
        IIngredientCollection<ComplexStack, Integer> empty1c = IngredientCollections.emptyCollection(IngredientComponentStubs.COMPLEX);
        IngredientArrayList<Integer, Boolean> cols1 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        IngredientArrayList<Integer, Boolean> cols2 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        IngredientArrayList<Integer, Boolean> cols3 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1));
        IngredientArrayList<Integer, Boolean> cols4 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 3));
        IngredientArrayList<ComplexStack, Integer> colc1 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 2, 0, ComplexStack.Tag.A)
        ));
        IngredientArrayList<ComplexStack, Integer> colc2 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 2, 0, ComplexStack.Tag.A)
        ));
        IngredientArrayList<ComplexStack, Integer> colc3 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A)
        ));
        IngredientArrayList<ComplexStack, Integer> colc4 = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList(
                new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 1, 0, ComplexStack.Tag.A),
                new ComplexStack(ComplexStack.Group.A, 3, 0, ComplexStack.Tag.A)
        ));

        assertThat(IngredientCollections.hash(empty1), instanceOf(int.class));

        assertThat(IngredientCollections.hash(empty1), is(IngredientCollections.hash(empty1)));
        assertThat(IngredientCollections.hash(empty1), is(IngredientCollections.hash(empty2)));

        assertThat(IngredientCollections.hash(empty2), is(IngredientCollections.hash(empty1)));
        assertThat(IngredientCollections.hash(empty2), is(IngredientCollections.hash(empty2)));

        assertThat(IngredientCollections.hash(empty1c), is(IngredientCollections.hash(empty1c)));

        assertThat(IngredientCollections.hash(cols1), is(IngredientCollections.hash(cols1)));
        assertThat(IngredientCollections.hash(cols1), is(IngredientCollections.hash(cols2)));

        assertThat(IngredientCollections.hash(cols2), is(IngredientCollections.hash(cols1)));
        assertThat(IngredientCollections.hash(cols2), is(IngredientCollections.hash(cols2)));

        assertThat(IngredientCollections.hash(cols3), is(IngredientCollections.hash(cols3)));

        assertThat(IngredientCollections.hash(cols4), is(IngredientCollections.hash(cols4)));

        assertThat(IngredientCollections.hash(colc1), is(IngredientCollections.hash(colc1)));
        assertThat(IngredientCollections.hash(colc1), is(IngredientCollections.hash(colc2)));

        assertThat(IngredientCollections.hash(colc2), is(IngredientCollections.hash(colc1)));
        assertThat(IngredientCollections.hash(colc2), is(IngredientCollections.hash(colc2)));

        assertThat(IngredientCollections.hash(colc3), is(IngredientCollections.hash(colc3)));

        assertThat(IngredientCollections.hash(colc4), is(IngredientCollections.hash(colc4)));
    }

    @Test
    public void testToString() {
        IIngredientCollection<Integer, Boolean> empty1 = IngredientCollections.emptyCollection(IngredientComponentStubs.SIMPLE);
        IIngredientCollection<Integer, Boolean> empty2 = IngredientCollections.emptyCollection(IngredientComponentStubs.SIMPLE);
        IIngredientCollection<ComplexStack, Integer> empty1c = IngredientCollections.emptyCollection(IngredientComponentStubs.COMPLEX);
        IngredientArrayList<Integer, Boolean> cols1 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        IngredientArrayList<Integer, Boolean> cols2 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        IngredientArrayList<Integer, Boolean> cols3 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1));
        IngredientArrayList<Integer, Boolean> cols4 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 3));

        assertThat(IngredientCollections.toString(empty1), equalTo("[]"));
        assertThat(IngredientCollections.toString(empty2), equalTo("[]"));
        assertThat(IngredientCollections.toString(empty1c), equalTo("[]"));
        assertThat(IngredientCollections.toString(cols1), equalTo("[0, 1, 2]"));
        assertThat(IngredientCollections.toString(cols2), equalTo("[0, 1, 2]"));
        assertThat(IngredientCollections.toString(cols3), equalTo("[0, 1]"));
        assertThat(IngredientCollections.toString(cols4), equalTo("[0, 1, 3]"));
    }

}
