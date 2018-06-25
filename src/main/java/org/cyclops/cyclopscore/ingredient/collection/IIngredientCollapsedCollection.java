package org.cyclops.cyclopscore.ingredient.collection;

/**
 * An ingredient collection that does not strictly adhere to the {@link IIngredientCollection} interface
 * and will collapse ingredients that are equal, ignoring their quantity.
 * This collapsing will result in quantities of equal ingredients to be summed up.
 *
 * For example, equal ItemStacks with different counts can be added,
 * but only a single ItemStack will be present in the collection,
 * with as count the sum of the given counts.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public interface IIngredientCollapsedCollection<T, M> extends IIngredientCollection<T, M> {
}
