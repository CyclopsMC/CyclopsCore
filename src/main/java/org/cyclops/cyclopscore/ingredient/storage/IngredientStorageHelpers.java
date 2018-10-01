package org.cyclops.cyclopscore.ingredient.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollection;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionPrototypeMap;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollections;

import javax.annotation.Nullable;
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
                return insertIngredientRemainderFixup(source, destination, extracted, false);
            }
        }
        return matcher.getEmptyInstance();
    }

    /**
     * Iteratively move the instance that matches the given match condition from source to destination.
     * The quantity of the given instance indicates the maximum amount that can be moved.
     *
     * This is useful in cases that the internal transfer rate of certain storages have to be overridden.
     *
     * Note: When simulating, only a single iteration will be done.
     * This is because the iterations don't actually take effect,
     * which could cause infinite loops.
     *
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param instance The prototype instance.
     * @param matchCondition The match condition.
     * @param simulate If the movement should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredientsIterative(IIngredientComponentStorage<T, M> source,
                                                    IIngredientComponentStorage<T, M> destination,
                                                    T instance, M matchCondition, boolean simulate) {
        IngredientComponent<T, M> component = source.getComponent();
        IIngredientMatcher<T, M> matcher = component.getMatcher();
        long maxQuantity = matcher.getQuantity(instance);
        T movedFirst = moveIngredients(source, destination, instance, matchCondition, simulate);
        long movedQuantity = matcher.getQuantity(movedFirst);
        if (simulate || movedQuantity == 0) {
            return movedFirst;
        }

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
        Iterator<T> it = source.iterator(instance, matcher.withoutCondition(matchCondition,
                source.getComponent().getPrimaryQuantifier().getMatchCondition()));
        long prototypeQuantity = matcher.getQuantity(instance);
        while (it.hasNext()) {
            T extractedSimulated = it.next();
            if (matcher.getQuantity(extractedSimulated) != prototypeQuantity) {
                extractedSimulated = matcher.withQuantity(extractedSimulated, prototypeQuantity);
            }
            T moved = moveIngredient(source, destination, extractedSimulated, matchCondition, simulate);
            if (!matcher.isEmpty(moved)) {
                return moved;
            }
        }
        return matcher.getEmptyInstance();
    }

    /**
     * Move the first instance that matches the given predicate from source to destination.
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param predicate The predicate to match instances by.
     * @param maxQuantity The max quantity that can be moved.
     * @param exactQuantity If the max quantity should be interpreted as an exact quantity.
     * @param simulate If the movement should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredients(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           Predicate<T> predicate, long maxQuantity, boolean exactQuantity,
                                           boolean simulate) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();
        for (T extractedSimulated : source) {
            if (predicate.test(extractedSimulated)) {
                if (matcher.getQuantity(extractedSimulated) > maxQuantity) {
                    extractedSimulated = matcher.withQuantity(extractedSimulated, maxQuantity);
                }
                extractedSimulated = source.extract(extractedSimulated, matcher.getExactMatchNoQuantityCondition(), true);
                if (!matcher.isEmpty(extractedSimulated)) {
                    T movable = insertIngredient(destination, extractedSimulated, true);
                    if (!matcher.isEmpty(movable)
                            && (exactQuantity ? matcher.getQuantity(movable) == maxQuantity
                            : matcher.getQuantity(movable) <= maxQuantity)) {
                        if (simulate) {
                            return movable;
                        } else {
                            T extracted = source.extract(movable, matcher.getExactMatchNoQuantityCondition(), false);
                            return insertIngredientRemainderFixup(source, destination, extracted, false);
                        }
                    }
                }
            }
        }
        return matcher.getEmptyInstance();
    }

    /**
     * Move ingredients from source to target with optional source and target slots,
     * based on an ingredient prototype and match condition.
     *
     * If the algorithm should iterate over all source/destination slot,
     * then the respective slot should be -1.
     *
     * If a slot is defined, and the storage is not an instance of {@link IIngredientComponentStorageSlotted},
     * then nothing will be moved.
     *
     * @param source A source storage to extract from.
     * @param sourceSlot The source slot or -1 for any.
     * @param destination A destination storage to insert to.
     * @param destinationSlot The destination slot or -1 for any.
     * @param instance The prototype instance.
     * @param matchCondition The match condition.
     * @param simulate If the movement should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredientsSlotted(IIngredientComponentStorage<T, M> source, int sourceSlot,
                                                  IIngredientComponentStorage<T, M> destination, int destinationSlot,
                                                  T instance, M matchCondition, boolean simulate) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();

        // Optimization if nothing will be moved in any case
        if (matcher.getQuantity(instance) <= 0) {
            return matcher.getEmptyInstance();
        }

        boolean loopSourceSlots = sourceSlot < 0;
        boolean loopDestinationSlots = destinationSlot < 0;

        if (!loopSourceSlots && !loopDestinationSlots) {
            // Both source and destination slot are defined

            // Fail if source or destination are not slotted
            if (!(source instanceof IIngredientComponentStorageSlotted)) {
                return matcher.getEmptyInstance();
            }
            if (!(destination instanceof IIngredientComponentStorageSlotted)) {
                return matcher.getEmptyInstance();
            }
            IIngredientComponentStorageSlotted<T, M> sourceSlotted = (IIngredientComponentStorageSlotted<T, M>) source;
            IIngredientComponentStorageSlotted<T, M> destinationSlotted = (IIngredientComponentStorageSlotted<T, M>) destination;

            // Extract from source slot (simulated)
            long prototypeQuantity = matcher.getQuantity(instance);
            T extractedSimulated = sourceSlotted.extract(sourceSlot, prototypeQuantity, true);
            if (!matcher.isEmpty(extractedSimulated) && matcher.matches(instance, extractedSimulated, matchCondition)) {
                // Insert into target slot  (simulated)
                T remaining = destinationSlotted.insert(destinationSlot, extractedSimulated, true);
                long remainingQuantity = matcher.getQuantity(remaining);
                if (remainingQuantity == 0 ||
                        (remainingQuantity < prototypeQuantity && !matcher.hasCondition(matchCondition,
                                source.getComponent().getPrimaryQuantifier().getMatchCondition()))) {
                    return moveEffectiveSourceExactDestinationExact(sourceSlotted, sourceSlot, destinationSlotted, destinationSlot,
                            extractedSimulated, remaining, simulate);
                }
            }
        } else if (loopSourceSlots) {
            if (source instanceof IIngredientComponentStorageSlotted) {
                // Recursively call movement logic for each slot in the source if slotted.
                IIngredientComponentStorageSlotted<T, M> sourceSlotted = (IIngredientComponentStorageSlotted<T, M>) source;
                int slots = sourceSlotted.getSlots();
                for (int slot = 0; slot < slots; slot++) {
                    T moved = moveIngredientsSlotted(source, slot, destination, destinationSlot, instance, matchCondition, simulate);
                    if (!matcher.isEmpty(moved)) {
                        return moved;
                    }
                }
            } else {
                // If we don't have source slots, iterate over all source slot instances in a slotless way
                long prototypeQuantity = matcher.getQuantity(instance);
                if (loopDestinationSlots) {
                    return moveIngredients(source, destination, instance, matchCondition, simulate);
                } else {
                    if (!(destination instanceof IIngredientComponentStorageSlotted)) {
                        return matcher.getEmptyInstance();
                    }
                    IIngredientComponentStorageSlotted<T, M> destinationSlotted = (IIngredientComponentStorageSlotted<T, M>) destination;
                    for (T sourceInstance : source) {
                        if (matcher.matches(instance, sourceInstance, matcher.withoutCondition(matchCondition,
                                source.getComponent().getPrimaryQuantifier().getMatchCondition()))) {
                            if (matcher.getQuantity(sourceInstance) != prototypeQuantity) {
                                sourceInstance = matcher.withQuantity(sourceInstance, prototypeQuantity);
                            }
                            T extractedSimulated = source.extract(sourceInstance, matchCondition, true);
                            if (!matcher.isEmpty(extractedSimulated)) {
                                T remaining = destinationSlotted.insert(destinationSlot, extractedSimulated, true);
                                long remainingQuantity = matcher.getQuantity(remaining);
                                if (remainingQuantity == 0 ||
                                        (remainingQuantity < prototypeQuantity && !matcher.hasCondition(matchCondition,
                                                source.getComponent().getPrimaryQuantifier().getMatchCondition()))) {
                                    T moved = moveEffectiveSourceNonSlottedDestinationExact(source, destinationSlotted,
                                            destinationSlot, matchCondition, extractedSimulated, remaining, simulate);
                                    if (moved != null) {
                                        return moved;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else { // loopDestinationSlots && !loopSourceSlots
            // Quickly break if the source is not slotted.
            if (!(source instanceof IIngredientComponentStorageSlotted)) {
                return matcher.getEmptyInstance();
            }

            if (destination instanceof IIngredientComponentStorageSlotted) {
                // Recursively call movement logic for each slot in the destination if slotted.
                IIngredientComponentStorageSlotted<T, M> destinationSlotted = (IIngredientComponentStorageSlotted<T, M>) destination;
                int slots = destinationSlotted.getSlots();
                for (int slot = 0; slot < slots; slot++) {
                    T moved = moveIngredientsSlotted(source, sourceSlot, destination, slot, instance, matchCondition, simulate);
                    if (!matcher.isEmpty(moved)) {
                        return moved;
                    }
                }
            } else {
                // If we don't have destination slots, move from defined source slot
                IIngredientComponentStorageSlotted<T, M> sourceSlotted = (IIngredientComponentStorageSlotted<T, M>) source;
                long prototypeQuantity = matcher.getQuantity(instance);
                T sourceInstance = sourceSlotted.extract(sourceSlot, prototypeQuantity, true);
                if (!matcher.isEmpty(sourceInstance) && matcher.matches(instance, sourceInstance, matchCondition)) {
                    T inserted = insertIngredient(destination, sourceInstance, true);
                    return moveEffectiveSourceExactDestinationNonSlotted(sourceSlotted, sourceSlot, destination, inserted, simulate);
                }
            }
        }
        return matcher.getEmptyInstance();
    }

    /**
     * Move ingredients from source to target with optional source and target slots,
     * based on an ingredient predicate.
     *
     * If the algorithm should iterate over all source/destination slot,
     * then the respective slot should be -1.
     *
     * If a slot is defined, and the storage is not an instance of {@link IIngredientComponentStorageSlotted},
     * then nothing will be moved.
     *
     * @param source A source storage to extract from.
     * @param sourceSlot The source slot or -1 for any.
     * @param destination A destination storage to insert to.
     * @param destinationSlot The destination slot or -1 for any.
     * @param predicate The instance predicate.
     * @param maxQuantity The max quantity that can be moved.
     * @param exactQuantity If the max quantity should be interpreted as an exact quantity.
     * @param simulate If the movement should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredientsSlotted(IIngredientComponentStorage<T, M> source, int sourceSlot,
                                                  IIngredientComponentStorage<T, M> destination, int destinationSlot,
                                                  Predicate<T> predicate, long maxQuantity, boolean exactQuantity,
                                                  boolean simulate) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();
        boolean loopSourceSlots = sourceSlot < 0;
        boolean loopDestinationSlots = destinationSlot < 0;

        if (!loopSourceSlots && !loopDestinationSlots) {
            // Both source and destination slot are defined

            // Fail if source or destination are not slotted
            if (!(source instanceof IIngredientComponentStorageSlotted)) {
                return matcher.getEmptyInstance();
            }
            if (!(destination instanceof IIngredientComponentStorageSlotted)) {
                return matcher.getEmptyInstance();
            }
            IIngredientComponentStorageSlotted<T, M> sourceSlotted = (IIngredientComponentStorageSlotted<T, M>) source;
            IIngredientComponentStorageSlotted<T, M> destinationSlotted = (IIngredientComponentStorageSlotted<T, M>) destination;

            // Extract from source slot (simulated)
            T extractedSimulated = sourceSlotted.extract(sourceSlot, maxQuantity, true);
            if (!matcher.isEmpty(extractedSimulated) && predicate.test(extractedSimulated)
                    && (!exactQuantity || matcher.getQuantity(extractedSimulated) == maxQuantity)) {
                // Insert into target slot  (simulated)
                T remaining = destinationSlotted.insert(destinationSlot, extractedSimulated, true);
                long remainingQuantity = matcher.getQuantity(remaining);
                if (remainingQuantity == 0 || (remainingQuantity < maxQuantity && !exactQuantity)) {
                    return moveEffectiveSourceExactDestinationExact(sourceSlotted, sourceSlot, destinationSlotted, destinationSlot,
                            extractedSimulated, remaining, simulate);
                }
            }
        } else if (loopSourceSlots) {
            if (source instanceof IIngredientComponentStorageSlotted) {
                // Recursively call movement logic for each slot in the source if slotted.
                IIngredientComponentStorageSlotted<T, M> sourceSlotted = (IIngredientComponentStorageSlotted<T, M>) source;
                int slots = sourceSlotted.getSlots();
                for (int slot = 0; slot < slots; slot++) {
                    T moved = moveIngredientsSlotted(source, slot, destination, destinationSlot, predicate, maxQuantity, exactQuantity, simulate);
                    if (!matcher.isEmpty(moved)) {
                        return moved;
                    }
                }
            } else {
                // If we don't have source slots, iterate over all source slot instances in a slotless way
                if (loopDestinationSlots) {
                    return moveIngredients(source, destination, predicate, maxQuantity, exactQuantity, simulate);
                } else {
                    if (!(destination instanceof IIngredientComponentStorageSlotted)) {
                        return matcher.getEmptyInstance();
                    }
                    IIngredientComponentStorageSlotted<T, M> destinationSlotted = (IIngredientComponentStorageSlotted<T, M>) destination;
                    for (T sourceInstance : source) {
                        if (predicate.test(sourceInstance)) {
                            if (matcher.getQuantity(sourceInstance) != maxQuantity) {
                                sourceInstance = matcher.withQuantity(sourceInstance, maxQuantity);
                            }
                            T extractedSimulated = source.extract(sourceInstance, matcher.getExactMatchCondition(), true);
                            if (!matcher.isEmpty(extractedSimulated)) {
                                T remaining = destinationSlotted.insert(destinationSlot, extractedSimulated, true);
                                long remainingQuantity = matcher.getQuantity(remaining);
                                if (remainingQuantity == 0 || (remainingQuantity < maxQuantity && !exactQuantity)) {
                                    T moved = moveEffectiveSourceNonSlottedDestinationExact(source, destinationSlotted,
                                            destinationSlot, matcher.getExactMatchCondition(), extractedSimulated, remaining, simulate);
                                    if (moved != null) {
                                        return moved;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else { // loopDestinationSlots && !loopSourceSlots
            // Quickly break if the source is not slotted.
            if (!(source instanceof IIngredientComponentStorageSlotted)) {
                return matcher.getEmptyInstance();
            }

            if (destination instanceof IIngredientComponentStorageSlotted) {
                // Recursively call movement logic for each slot in the destination if slotted.
                IIngredientComponentStorageSlotted<T, M> destinationSlotted = (IIngredientComponentStorageSlotted<T, M>) destination;
                int slots = destinationSlotted.getSlots();
                for (int slot = 0; slot < slots; slot++) {
                    T moved = moveIngredientsSlotted(source, sourceSlot, destination, slot, predicate, maxQuantity, exactQuantity, simulate);
                    if (!matcher.isEmpty(moved)) {
                        return moved;
                    }
                }
            } else {
                // If we don't have destination slots, move from defined source slot
                IIngredientComponentStorageSlotted<T, M> sourceSlotted = (IIngredientComponentStorageSlotted<T, M>) source;
                T sourceInstance = sourceSlotted.extract(sourceSlot, maxQuantity, true);
                if (!matcher.isEmpty(sourceInstance) && predicate.test(sourceInstance)) {
                    T inserted = insertIngredient(destination, sourceInstance, true);
                    if (exactQuantity && matcher.getQuantity(inserted) != maxQuantity) {
                        return matcher.getEmptyInstance();
                    }
                    return moveEffectiveSourceExactDestinationNonSlotted(sourceSlotted, sourceSlot, destination, inserted, simulate);
                }
            }
        }
        return matcher.getEmptyInstance();
    }

    protected static <T, M> T moveEffectiveSourceExactDestinationExact(IIngredientComponentStorageSlotted<T, M> sourceSlotted, int sourceSlot,
                                                                       IIngredientComponentStorageSlotted<T, M> destinationSlotted, int destinationSlot,
                                                                       T extractedSimulated, T remaining, boolean simulate) {
        IIngredientMatcher<T, M> matcher = sourceSlotted.getComponent().getMatcher();
        if (simulate) {
            // Return the result if we intended to simulate
            if (matcher.getQuantity(remaining) == 0) {
                return extractedSimulated;
            } else {
                return matcher.withQuantity(extractedSimulated,
                        matcher.getQuantity(extractedSimulated) - matcher.getQuantity(remaining));
            }
        } else {
            // Redo the operation if we do not intend to simulate
            long movedQuantitySimulated = matcher.getQuantity(extractedSimulated) - matcher.getQuantity(remaining);
            T sourceInstanceEffective = sourceSlotted.extract(sourceSlot, movedQuantitySimulated, false);
            // The following should always be true. If not, then the source was lying during simulated mode.
            // But we can safely ignore those cases at this point as nothing has been moved yet.
            if (!matcher.isEmpty(sourceInstanceEffective)) {
                // Remaining should be empty, otherwise the destination was lying during simulated mode
                T remainingEffective = destinationSlotted.insert(destinationSlot, sourceInstanceEffective, false);
                if (matcher.isEmpty(remainingEffective)) {
                    return sourceInstanceEffective;
                } else {
                    // If the destination was lying, try to add the remainder back into the source.
                    // If even that fails, throw an error.
                    T remainderFixup = sourceSlotted.insert(sourceSlot, remainingEffective, false);
                    if (matcher.isEmpty(remainderFixup)) {
                        // We've managed to fix the problem, calculate the effective instance that was moved.
                        return matcher.withQuantity(remainingEffective,
                                matcher.getQuantity(sourceInstanceEffective)
                                        - matcher.getQuantity(remainingEffective));
                    } else {
                        throw new IllegalStateException("Effective slotted source to slotted destination movement failed " +
                                "due to inconsistent insertion behaviour by destination in simulation " +
                                "and non-simulation: " + destinationSlotted + ". Lost: " + remainderFixup);
                    }
                }
            }
        }
        return matcher.getEmptyInstance();
    }

    protected static <T, M> T moveEffectiveSourceExactDestinationNonSlotted(IIngredientComponentStorageSlotted<T, M> sourceSlotted, int sourceSlot,
                                                                            IIngredientComponentStorage<T, M> destination,
                                                                            T inserted, boolean simulate) {
        IIngredientMatcher<T, M> matcher = sourceSlotted.getComponent().getMatcher();
        if (simulate) {
            return inserted;
        } else if (!matcher.isEmpty(inserted)) {
            T sourceInstanceEffective = sourceSlotted.extract(sourceSlot, matcher.getQuantity(inserted), false);
            return insertIngredientRemainderFixup(sourceSlotted, destination, sourceInstanceEffective, false);
        }
        return matcher.getEmptyInstance();
    }

    @Nullable
    protected static <T, M> T moveEffectiveSourceNonSlottedDestinationExact(IIngredientComponentStorage<T, M> source,
                                                                            IIngredientComponentStorageSlotted<T, M> destinationSlotted, int destinationSlot,
                                                                            M extractMatchCondition, T extractedSimulated, T remaining, boolean simulate) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();

        // Set the movable instance
        T shouldMove;
        if (matcher.isEmpty(remaining)) {
            shouldMove = extractedSimulated;
        } else {
            shouldMove = matcher.withQuantity(extractedSimulated,
                    matcher.getQuantity(extractedSimulated) - matcher.getQuantity(remaining));
        }

        if (simulate) {
            // Return the result if we intended to simulate
            return shouldMove;
        } else {
            T extractedEffective = source.extract(shouldMove, extractMatchCondition, false);
            // The following should always be true. If not, then the source was lying during simulated mode.
            // But we can safely ignore those cases at this point as nothing has been moved yet.
            if (!matcher.isEmpty(extractedSimulated)) {
                // Remaining should be empty, otherwise the destination was lying during simulated mode
                T remainingEffective = destinationSlotted.insert(destinationSlot, extractedEffective, false);
                return handleRemainder(source, destinationSlotted, extractedEffective, remainingEffective);
            }
        }

        return null;
    }

    public static <T, M> T handleRemainder(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           T movedEffective,
                                           T remainingEffective) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();
        boolean remainingEffectiveEmpty = matcher.isEmpty(remainingEffective);
        if (remainingEffectiveEmpty) {
            return movedEffective;
        } else {
            // If the destination was lying, try to add the remainder back into the source.
            // If even that fails, throw an error.
            T remainderFixup = source.insert(remainingEffective, false);
            if (matcher.isEmpty(remainderFixup)) {
                // We've managed to fix the problem, calculate the effective instance that was moved.
                return matcher.withQuantity(remainingEffective,
                        matcher.getQuantity(movedEffective)
                                - matcher.getQuantity(remainingEffective));
            } else {
                throw new IllegalStateException("Slotless source to destination movement failed " +
                        "due to inconsistent insertion behaviour by destination in simulation " +
                        "and non-simulation: " + destination + ". Lost: " + remainderFixup);
            }
        }
    }

    /**
     * Move the first instance that matches the given match condition from source to destination.
     *
     * The main difference of this method to
     * {@link #moveIngredients(IIngredientComponentStorage, IIngredientComponentStorage, Object, Object, boolean)}
     * is that the latter method will try checking *multiple* ingredients from the source,
     * while this method will only check the *first matching* ingredient.
     * This makes this method potentially more efficient than the latter.
     *
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param instance The prototype instance.
     * @param matchCondition The match condition.
     * @param simulate If the movement should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredient(IIngredientComponentStorage<T, M> source,
                                          IIngredientComponentStorage<T, M> destination,
                                          T instance, M matchCondition, boolean simulate) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();
        T extractedSimulated = source.extract(instance, matchCondition, true);
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
                    return insertIngredientRemainderFixup(source, destination, extracted, false);
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

    /**
     * Insert an ingredient in a destination storage.
     * If not in simulation mode, and the instance does not completely fit into the destination,
     * the remainder will be inserted into the source.
     * If not everything can be re-inserted into the source,
     * and {@link IllegalStateException} will be thrown.
     *
     * The difference of this method compared to {@link IIngredientComponentStorage#insert(Object, boolean)}
     * is that this method returns the actually inserted ingredient
     * instead of the remaining ingredient that was not inserted.
     *
     * @param source A source storage to insert fixup instances.
     * @param destination A storage.
     * @param instance An instance to insert.
     * @param simulate If the insertion should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The actual inserted ingredient, or would-be inserted ingredient if simulated.
     */
    public static <T, M> T insertIngredientRemainderFixup(IIngredientComponentStorage<T, M> source,
                                                          IIngredientComponentStorage<T, M> destination,
                                                          T instance, boolean simulate) {
        if (simulate) {
            return insertIngredient(destination, instance, true);
        }
        T remainingEffective = destination.insert(instance, false);
        return handleRemainder(source, destination, instance, remainingEffective);
    }

    /**
     * Serialize the given storage to NBT.
     *
     * All ingredients, the max quantity, and whether or not it is slotted will be stored.
     *
     * @param storage An ingredient storage.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return An NBT tag.
     */
    public static <T, M> NBTTagCompound serialize(IIngredientComponentStorage<T, M> storage) {
        NBTTagCompound tag = IngredientCollections.serialize(new IngredientArrayList<>(storage.getComponent(), storage.iterator()));
        tag.setLong("maxQuantity", storage.getMaxQuantity());
        tag.setBoolean("slotted", storage instanceof IIngredientComponentStorageSlotted);
        return tag;
    }

    /**
     * Deserialize the storage from the given NBT tag.
     *
     * All ingredients, the max quantity, and whether or not it is slotted will be restored.
     *
     * @param tag An NBT tag.
     * @param rateLimit The rate limit per insertion/extraction.
     * @return The deserialized storage.
     * @throws IllegalArgumentException If the tag was invalid.
     */
    public static IIngredientComponentStorage<?, ?> deserialize(NBTTagCompound tag, long rateLimit) {
        if (!tag.hasKey("maxQuantity", Constants.NBT.TAG_LONG)) {
            throw new IllegalArgumentException("No maxQuantity was found in the given tag");
        }
        if (!tag.hasKey("slotted", Constants.NBT.TAG_BYTE)) {
            throw new IllegalArgumentException("No slotted was found in the given tag");
        }
        long maxQuantity = tag.getLong("maxQuantity");
        if (tag.getBoolean("slotted")) {
            return new IngredientComponentStorageSlottedCollectionWrapper<>(
                    IngredientCollections.deserialize(tag, IngredientArrayList::new), maxQuantity, rateLimit);
        } else {
            return new IngredientComponentStorageCollectionWrapper<>(
                    IngredientCollections.deserialize(tag, IngredientCollectionPrototypeMap::new), maxQuantity, rateLimit);
        }
    }

    /**
     * Helper interface for constructing an {@link IIngredientCollection} based on an {@link IngredientComponent}.
     * @param <C>
     */
    public static interface IIngredientStorageConstructor<C extends IIngredientComponentStorage<?, ?>> {
        public <T, M> C create(IngredientComponent<T, M> ingredientComponent);
    }

}
