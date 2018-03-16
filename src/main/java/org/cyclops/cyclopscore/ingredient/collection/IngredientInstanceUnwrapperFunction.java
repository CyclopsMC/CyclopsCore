package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.base.Function;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;

import javax.annotation.Nullable;

/**
 * A function that unwraps wrapped instances into instances.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientInstanceUnwrapperFunction<T, M> implements Function<IngredientInstanceWrapper<T, M>, T> {

    private static final IngredientInstanceUnwrapperFunction<?, ?> INSTANCE = new IngredientInstanceUnwrapperFunction<>();

    private IngredientInstanceUnwrapperFunction() {

    }

    public static <T, M> IngredientInstanceUnwrapperFunction<T, M> getInstance() {
        return (IngredientInstanceUnwrapperFunction<T, M>) INSTANCE;
    }

    @Nullable
    @Override
    public T apply(@Nullable IngredientInstanceWrapper<T, M> input) {
        return input.getInstance();
    }
}
