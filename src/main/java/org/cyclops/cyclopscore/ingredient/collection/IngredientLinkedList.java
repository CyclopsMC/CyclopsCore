package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.LinkedList;

/**
 * An ingredient list collection that internally uses an {@link LinkedList} to store instances.
 * @see LinkedList
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientLinkedList<T, M> extends IngredientList<T, M> {

    public IngredientLinkedList(IngredientComponent<T, M> component) {
        super(component, new LinkedList<>());
    }

    public IngredientLinkedList(IngredientComponent<T, M> component, Iterable<? extends T> iterable) {
        super(component, Lists.newLinkedList(iterable));
    }

}
