package org.cyclops.cyclopscore.ingredient.collection.diff;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollapsedCollectionMutable;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollectionMutable;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionPrototypeMap;
import org.cyclops.cyclopscore.ingredient.collection.IngredientLinkedList;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

/**
 * Helper methods for ingredient collection diffs.
 * @see IngredientCollectionDiffManager
 * @author rubensworks
 */
public class IngredientCollectionDiffHelpers {

    /**
     * Calculate a diff between <var>oldInstancesCache</var> - <var>newInstances</var>,
     * and produce a diff.
     * Additionally, this will populate <var>newInstancesCache</var>
     * with all instances from <var>newInstances</var>.
     *
     * @param ingredientComponent   The ingredient component type.
     * @param oldInstancesCache     The collection of instances to take the difference from.
     * @param newInstancesCache     An empty collection to store our new instances from <var>newInstances</var>.
     *                              This should be used as <var>oldInstancesCache</var> parameter on the next call.
     *                              This map MUST accept negative instances.
     *                              of this method.
     * @param newInstances          The new instances to calculate the difference with.
     * @param <T>                   The instance type.
     * @param <M>                   The matching condition parameter.
     * @return The resulting diff between <var>oldInstancesCache</var> and <var>newInstances</var>.
     */
    public static <T, M> IngredientCollectionDiff<T, M> getDiff(IngredientComponent<T, M> ingredientComponent,
                                                                @Nullable IngredientCollectionPrototypeMap<T, M> oldInstancesCache,
                                                                IngredientCollectionPrototypeMap<T, M> newInstancesCache,
                                                                Iterator<T> newInstances) {
        // Remove new instances from last instance collection
        // In the meantime, also construct the instance collection for next iteration
        IIngredientMatcher<T, M> matcher = ingredientComponent.getMatcher();
        boolean completelyEmpty = !newInstances.hasNext();
        while (newInstances.hasNext()) {
            T newInstance = newInstances.next();
            if (!matcher.isEmpty(newInstance)) {
                if (oldInstancesCache != null) {
                    oldInstancesCache.remove(newInstance);
                }
                newInstancesCache.add(newInstance);
            }
        }

        // Calculate addition and deletion collections
        IIngredientCollectionMutable<T, M> additions = new IngredientLinkedList<>(ingredientComponent);
        IIngredientCollectionMutable<T, M> deletions = new IngredientLinkedList<>(ingredientComponent);
        if (oldInstancesCache != null) {
            Iterator<Map.Entry<T, Long>> quantitativeIterator = oldInstancesCache.prototypeIterator();
            while (quantitativeIterator.hasNext()) {
                Map.Entry<T, Long> entry = quantitativeIterator.next();
                long count = entry.getValue();
                if (count < 0) {
                    additions.add(ingredientComponent.getMatcher().withQuantity(entry.getKey(), -count));
                } else if (count > 0) {
                    deletions.add(ingredientComponent.getMatcher().withQuantity(entry.getKey(), count));
                }
            }
        } else {
            additions = newInstancesCache;
        }

        return new IngredientCollectionDiff<>(additions, deletions, completelyEmpty);
    }

    /**
     * Apply the given diff to the given collection.
     * @param ingredientComponent   The ingredient component type.
     * @param diff                  The diff to apply.
     * @param collection            The collection to apply the diff to.
     * @param <T>                   The instance type.
     * @param <M>                   The matching condition parameter.
     */
    public static <T, M> void applyDiff(IngredientComponent<T, M> ingredientComponent,
                                        IngredientCollectionDiff<T, M> diff,
                                        IIngredientCollectionMutable<T, M> collection) {
        boolean applyDirectly = collection instanceof IIngredientCollapsedCollectionMutable;
        IIngredientCollapsedCollectionMutable<T, M> prototypedIngredients;

        // Use a prototype-based collection so that ingredients are collapsed
        if (applyDirectly) {
            prototypedIngredients = (IIngredientCollapsedCollectionMutable<T, M>) collection;
        } else {
            prototypedIngredients = new IngredientCollectionPrototypeMap<>(ingredientComponent);
            prototypedIngredients.addAll(collection);
        }

        // Apply changes
        if (diff.hasAdditions()) {
            prototypedIngredients.addAll(diff.getAdditions());
        }
        if (diff.hasDeletions()){
            prototypedIngredients.removeAll(diff.getDeletions());
        }

        if (!applyDirectly) {
            // Persist changes
            collection.clear();
            collection.addAll(prototypedIngredients);
        }
    }

}
