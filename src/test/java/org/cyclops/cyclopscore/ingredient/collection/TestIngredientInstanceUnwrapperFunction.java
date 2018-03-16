package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientInstanceUnwrapperFunction {

    @Test
    public void testyApplySimple() {
        IngredientInstanceUnwrapperFunction<Integer, Boolean> unwrapper = IngredientInstanceUnwrapperFunction.getInstance();
        assertThat(unwrapper.apply(IngredientComponentStubs.SIMPLE.wrap(0)), is(0));
        assertThat(unwrapper.apply(IngredientComponentStubs.SIMPLE.wrap(1)), is(1));
    }

    @Test
    public void testyApplyComplex() {
        IngredientInstanceUnwrapperFunction<ComplexStack, Integer> unwrapper = IngredientInstanceUnwrapperFunction.getInstance();
        ComplexStack stack = new ComplexStack(ComplexStack.Group.A, 0, 0, ComplexStack.Tag.A);
        assertThat(unwrapper.apply(IngredientComponentStubs.COMPLEX.wrap(stack)), is(stack));
    }

}
