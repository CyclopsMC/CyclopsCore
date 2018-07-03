package org.cyclops.cyclopscore.ingredient.storage;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;

/**
 * Helper methods for moving ingredients between {@link IIngredientComponentStorage}'s.
 */
public final class IngredientStorageHelpers {

    /**
     * Move the given maximum quantity of instances from source to destination.
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param maxQuantity The maximum instance quantity to move.
     * @param simulate If the movement should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredients(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           int maxQuantity, boolean simulate) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();
        T extractedSimulated = source.extract(maxQuantity, true);
        long movableQuantity = insertIngredientQuantity(destination, extractedSimulated, true);
        if (movableQuantity > 0) {
            if (simulate) {
                if (maxQuantity == movableQuantity) {
                    return extractedSimulated;
                } else {
                    return matcher.withQuantity(extractedSimulated, movableQuantity);
                }
            } else {
                T extracted = source.extract(movableQuantity, false);
                return insertIngredient(destination, extracted, false);
            }
        }
        return matcher.getEmptyInstance();
    }

    /**
     * Move the first instance that matches the given match condition from source to destination.
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param instance The prototype instance.
     * @param matchCondition The match condition.
     * @param simulate If the movement should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredients(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           T instance, M matchCondition, boolean simulate) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();
        T extractedSimulated = source.extract(instance, matchCondition, true);
        long movableQuantity = insertIngredientQuantity(destination, extractedSimulated, true);
        if (movableQuantity > 0) {
            if (simulate) {
                if (matcher.getQuantity(instance) == movableQuantity) {
                    return extractedSimulated;
                } else {
                    return matcher.withQuantity(extractedSimulated, movableQuantity);
                }
            } else {
                T extracted = source.extract(instance, matchCondition, false);
                return insertIngredient(destination, extracted, false);
            }
        }
        return matcher.getEmptyInstance();
    }

    /**
     * Insert an ingredient in a destination storage.
     *
     * The difference of this method compared to {@link IIngredientComponentStorage#insert(Object, boolean)}
     * is that this method returns the actually inserted ingredient quantity
     * instead of the remaining ingredient that was not inserted.
     *
     * @param destination A storage.
     * @param instance An instance to insert.
     * @param simulate If the insertion should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The actual inserted ingredient quantity, or would-be inserted ingredient quantity if simulated.
     */
    public static <T, M> long insertIngredientQuantity(IIngredientComponentStorage<T, M> destination,
                                                       T instance, boolean simulate) {
        IIngredientMatcher<T, M> matcher = destination.getComponent().getMatcher();
        long quantity = matcher.getQuantity(instance);
        if (quantity > 0) {
            T remainingInserted = destination.insert(instance, simulate);
            long remainingInsertedQuantity = matcher.getQuantity(remainingInserted);
            return quantity - remainingInsertedQuantity;
        }
        return 0;
    }

    /**
     * Insert an ingredient in a destination storage.
     *
     * The difference of this method compared to {@link IIngredientComponentStorage#insert(Object, boolean)}
     * is that this method returns the actually inserted ingredient
     * instead of the remaining ingredient that was not inserted.
     *
     * @param destination A storage.
     * @param instance An instance to insert.
     * @param simulate If the insertion should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The actual inserted ingredient, or would-be inserted ingredient if simulated.
     */
    public static <T, M> T insertIngredient(IIngredientComponentStorage<T, M> destination,
                                            T instance, boolean simulate) {
        IIngredientMatcher<T, M> matcher = destination.getComponent().getMatcher();
        return matcher.withQuantity(instance, insertIngredientQuantity(destination, instance, simulate));
    }

}
