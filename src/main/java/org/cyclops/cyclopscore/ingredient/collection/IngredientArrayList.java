package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.ArrayList;

/**
 * An ingredient list collection that internally uses an {@link ArrayList} to store instances.
 * @see ArrayList
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientArrayList<T, M> extends IngredientList<T, M> {

    public IngredientArrayList(IngredientComponent<T, M> component) {
        super(component, new ArrayList<>());
    }

    public IngredientArrayList(IngredientComponent<T, M> component, int size) {
        super(component, new ArrayList<>(size));
    }

    public IngredientArrayList(IngredientComponent<T, M> component, Iterable<? extends T> iterable) {
        super(component, Lists.newArrayList(iterable));
    }

    public IngredientArrayList(IngredientComponent<T, M> component, T... instances) {
        super(component, Lists.newArrayList(instances));
    }

}
