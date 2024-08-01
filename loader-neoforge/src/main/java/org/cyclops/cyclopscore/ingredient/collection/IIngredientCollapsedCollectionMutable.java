package org.cyclops.cyclopscore.ingredient.collection;

/**
 * A mutable ingredient collection that does not strictly adhere to the {@link IIngredientCollection} interface
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
public interface IIngredientCollapsedCollectionMutable<T, M> extends IIngredientCollapsedCollection<T, M>,
        IIngredientCollectionMutable<T, M> {

    /**
     * Get the quantity of the given instance.
     * @param instance An instance, its quantity will be ignored.
     * @return The quantity.
     */
    public long getQuantity(T instance);

    /**
     * Set the quantity of the given instance.
     * @param instance An instance, its quantity will be ignored.
     * @param quantity The new quantity to set.
     */
    public void setQuantity(T instance, long quantity);

}
