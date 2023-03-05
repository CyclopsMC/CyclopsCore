package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

/**
 * @author rubensworks
 */
public class IngredientCollectionHelpers {

    /**
     * Create a new collapsed collection for the given ingredient component.
     * This collection will not be able to store negative quantities.
     *
     * @param ingredientComponent An ingredient component.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return A mutable collapsed ingredient collection.
     */
    public static <T, M> IIngredientCollapsedCollectionMutable<T, M> createCollapsedCollection(IngredientComponent<T, M> ingredientComponent) {
        if (ingredientComponent.getCategoryTypes().size() == 1) {
            new IngredientCollectionPrototypeMap<>(ingredientComponent);
        }
        return new IngredientCollectionSingleClassifiedCollapsed<>(
                ingredientComponent,
                () -> new IngredientCollectionPrototypeMap<>(ingredientComponent),
                ingredientComponent.getCategoryTypes().get(0));
    }

}
