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
            return new IngredientCollectionPrototypeMap<>(ingredientComponent);
        }
        return new IngredientCollectionSingleClassifiedCollapsed<>(
                ingredientComponent,
                () -> new IngredientCollectionPrototypeMap<>(ingredientComponent),
                ingredientComponent.getCategoryTypes().get(0));
    }

    /**
     * Create a new collapsed map for the given ingredient component.
     *
     * @param ingredientComponent An ingredient component.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @param <V> The map's value.
     * @return A mutable collapsed ingredient map.
     */
    public static <T, M, V> IIngredientMapMutable<T, M, V> createCollapsedMap(IngredientComponent<T, M> ingredientComponent) {
        if (ingredientComponent.getCategoryTypes().size() == 1) {
            return new IngredientHashMap<>(ingredientComponent);
        }
        return new IngredientMapSingleClassified<>(ingredientComponent, () -> new IngredientHashMap<>(ingredientComponent), ingredientComponent.getCategoryTypes().get(0));
    }

}
