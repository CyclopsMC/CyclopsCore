package org.cyclops.cyclopscore.ingredient.collection;

import java.util.List;

/**
 * An ingredient collection using list semantics.
 * This means that instances exist in a predefined order and that instances can exist multiple time in the collection.
 *
 * @see List
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public interface IIngredientList<T, M> extends IIngredientCollection<T, M> {
}
