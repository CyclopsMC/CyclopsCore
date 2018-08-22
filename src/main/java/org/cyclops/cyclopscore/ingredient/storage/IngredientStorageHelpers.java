package org.cyclops.cyclopscore.ingredient.storage;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Helper methods for moving ingredients between {@link IIngredientComponentStorage}'s.
 */
public final class IngredientStorageHelpers {

    /**
     * Iteratively move the given maximum quantity of instances from source to destination.
     *
     * This is useful in cases that the internal transfer rate of certain storages have to be overridden.
     *
     * Note: When simulating, only a single iteration will be done.
     * This is because the iterations don't actually take effect,
     * which could cause infinite loops.
     *
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param maxQuantity The maximum instance quantity to move.
     * @param simulate If the movement should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredientsIterative(IIngredientComponentStorage<T, M> source,
                                                    IIngredientComponentStorage<T, M> destination,
                                                    long maxQuantity, boolean simulate) {
        IngredientComponent<T, M> component = source.getComponent();
        IIngredientMatcher<T, M> matcher = component.getMatcher();
        T movedFirst = moveIngredients(source, destination, maxQuantity, simulate);
        long movedQuantity = matcher.getQuantity(movedFirst);
        if (simulate || movedQuantity == 0) {
            return movedFirst;
        }
        M matchCondition = matcher.getExactMatchNoQuantityCondition();

        // Try move until we reach the max quantity, or we don't move anything anymore.
        while (movedQuantity < maxQuantity) {
            T moved = moveIngredients(source, destination, movedFirst, matchCondition, false);
            if (matcher.isEmpty(moved)) {
                break;
            }
            movedQuantity += matcher.getQuantity(moved);
        }

        return matcher.withQuantity(movedFirst, movedQuantity);
    }

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
                                           long maxQuantity, boolean simulate) {
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
        Iterator<T> it = source.iterator(instance, matchCondition);
        long prototypeQuantity = matcher.getQuantity(instance);
        while (it.hasNext()) {
            T extractedSimulated = it.next();
            if (matcher.getQuantity(extractedSimulated) > prototypeQuantity) {
                extractedSimulated = matcher.withQuantity(extractedSimulated, prototypeQuantity);
            }
            extractedSimulated = source.extract(extractedSimulated, matchCondition, true);
            if (!matcher.isEmpty(extractedSimulated)) {
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
            }
        }
        return matcher.getEmptyInstance();
    }

    /**
     * Move the first instance that matches the given predicate from source to destination.
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param predicate The predicate to match instances by.
     * @param simulate If the movement should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredients(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           Predicate<T> predicate, boolean simulate) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();
        for (T extractedSimulated : source) {
            if (predicate.test(extractedSimulated)) {
                extractedSimulated = source.extract(extractedSimulated, matcher.getExactMatchNoQuantityCondition(), true);
                if (!matcher.isEmpty(extractedSimulated)) {
                    T movable = insertIngredient(destination, extractedSimulated, true);
                    if (!matcher.isEmpty(movable)) {
                        if (simulate) {
                            return movable;
                        } else {
                            T extracted = source.extract(movable, matcher.getExactMatchNoQuantityCondition(), false);
                            return insertIngredient(destination, extracted, false);
                        }
                    }
                }
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
