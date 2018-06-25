package org.cyclops.cyclopscore.ingredient.collection;

import java.util.Set;

/**
 * A ingredient collection using set semantics.
 * This means that each instances can only be present once in the collection based on its equals method.
 *
 * @see Set
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public interface IIngredientSet<T, M> {
}
