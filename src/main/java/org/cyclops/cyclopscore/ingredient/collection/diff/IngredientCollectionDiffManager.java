package org.cyclops.cyclopscore.ingredient.collection.diff;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollection;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionPrototypeMap;

import java.util.Iterator;

/**
 * Observes an {@link IIngredientCollection} to calculate diffs
 * each time {@link #onChange(Iterator)} is called.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @author rubensworks
 */
public class IngredientCollectionDiffManager<T, M> {

    private final IngredientComponent<T, M> ingredientComponent;
    private IngredientCollectionPrototypeMap<T, M> instancesCache;

    public IngredientCollectionDiffManager(IngredientComponent<T, M> ingredientComponent) {
        this.ingredientComponent = ingredientComponent;
        this.instancesCache = null;
    }

    /**
     * Calculates the diff between the given instances
     * and the instances from the last call of this method.
     *
     * When this method has not been called before,
     * it will emit a diff with all instances as additions.
     *
     * @param newInstances The new instances.
     * @return The diff.
     */
    public IngredientCollectionDiff<T, M> onChange(Iterator<T> newInstances) {
        IngredientCollectionPrototypeMap<T, M> newInstancesCache = new IngredientCollectionPrototypeMap<>(
                ingredientComponent, true);
        IngredientCollectionDiff<T, M> diff = IngredientCollectionDiffHelpers
                .getDiff(ingredientComponent, instancesCache, newInstancesCache, newInstances);
        this.instancesCache = newInstancesCache;
        return diff;
    }

}
