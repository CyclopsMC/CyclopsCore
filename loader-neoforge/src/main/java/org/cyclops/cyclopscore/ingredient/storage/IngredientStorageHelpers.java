package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Lists;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;
import org.cyclops.cyclopscore.ingredient.collection.*;

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
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param maxQuantity The maximum instance quantity to move.
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredientsIterative(IIngredientComponentStorage<T, M> source,
                                                    IIngredientComponentStorage<T, M> destination,
                                                    long maxQuantity, TransactionContext transaction) {
        IngredientComponent<T, M> component = source.getComponent();
        IIngredientMatcher<T, M> matcher = component.getMatcher();
        T movedFirst = moveIngredients(source, destination, maxQuantity, transaction);
        long movedFirstQuantity = matcher.getQuantity(movedFirst);
        long movedQuantity = movedFirstQuantity;
        if (movedQuantity == 0) {
            return movedFirst;
        }
        M matchCondition = matcher.getExactMatchNoQuantityCondition();

        // Try move until we reach the max quantity, or we don't move anything anymore.
        while (movedQuantity < maxQuantity) {
            // Reduce size if remainder is less than the first moved quantity
            long toMoveQuantity = maxQuantity - movedQuantity;
            if (toMoveQuantity < movedFirstQuantity) {
                movedFirst = matcher.withQuantity(movedFirst, toMoveQuantity);
            }

            T moved = moveIngredients(source, destination, movedFirst, matchCondition, transaction);
            if (matcher.isEmpty(moved)) {
                break;
            }
            movedQuantity += matcher.getQuantity(moved);
        }

        return matcher.withQuantity(movedFirst, movedQuantity);
    }

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
     * @throws InconsistentIngredientInsertionException When ingredients are lost due to inconsistent simulation.
     * @deprecated Use {@link #moveIngredientsIterative(IIngredientComponentStorage, IIngredientComponentStorage, long, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> T moveIngredientsIterative(IIngredientComponentStorage<T, M> source,
                                                    IIngredientComponentStorage<T, M> destination,
                                                    long maxQuantity, boolean simulate)
            throws InconsistentIngredientInsertionException {
        if (simulate) {
            // When simulating, only do one iteration to avoid infinite loops
            try (var tx = Transaction.openRoot()) {
                return moveIngredients(source, destination, maxQuantity, tx);
            }
        }
        try (var tx = Transaction.openRoot()) {
            T result = moveIngredientsIterative(source, destination, maxQuantity, tx);
            tx.commit();
            return result;
        }
    }

    /**
     * Move the given maximum quantity of instances from source to destination.
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param maxQuantity The maximum instance quantity to move.
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredients(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           long maxQuantity, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();

        // Simulate extraction to find out what ingredient type and how much can be extracted.
        // The simulation is rolled back so it does not affect the source.
        T simulatedExtracted;
        try (var simTx = Transaction.open(transaction)) {
            simulatedExtracted = source.extract(maxQuantity, simTx);
            // simTx not committed → rolled back
        }
        if (matcher.isEmpty(simulatedExtracted)) {
            return matcher.getEmptyInstance();
        }

        // In one real transaction: insert into destination, then extract only the accepted amount from source.
        // This avoids having to re-insert the remainder back into source (which may be extract-only).
        try (var nestedTx = Transaction.open(transaction)) {
            T remaining = destination.insert(simulatedExtracted, nestedTx);
            long movedQuantity = matcher.getQuantity(simulatedExtracted) - matcher.getQuantity(remaining);
            if (movedQuantity > 0) {
                T toExtract = matcher.withQuantity(simulatedExtracted, movedQuantity);
                T actualExtracted = source.extract(toExtract, matcher.getExactMatchCondition(), nestedTx);
                if (matcher.getQuantity(actualExtracted) == movedQuantity) {
                    nestedTx.commit();
                    return actualExtracted;
                }
            }
        }
        return matcher.getEmptyInstance();
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
     * @throws InconsistentIngredientInsertionException When ingredients are lost due to inconsistent simulation.
     * @deprecated Use {@link #moveIngredients(IIngredientComponentStorage, IIngredientComponentStorage, long, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> T moveIngredients(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           long maxQuantity, boolean simulate)
            throws InconsistentIngredientInsertionException {
        try (var tx = Transaction.openRoot()) {
            T result = moveIngredients(source, destination, maxQuantity, tx);
            if (!simulate) tx.commit();
            return result;
        }
    }

    /**
     * Iteratively move the instance that matches the given match condition from source to destination.
     * The quantity of the given instance indicates the maximum amount that can be moved.
     *
     * This is useful in cases that the internal transfer rate of certain storages have to be overridden.
     *
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param instance The prototype instance.
     * @param matchCondition The match condition.
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredientsIterative(IIngredientComponentStorage<T, M> source,
                                                    IIngredientComponentStorage<T, M> destination,
                                                    T instance, M matchCondition, TransactionContext transaction) {
        IngredientComponent<T, M> component = source.getComponent();
        IIngredientMatcher<T, M> matcher = component.getMatcher();
        long maxQuantity = matcher.getQuantity(instance);
        T movedFirst = moveIngredients(source, destination, instance, matchCondition, transaction);
        long movedFirstQuantity = matcher.getQuantity(movedFirst);
        long movedQuantity = movedFirstQuantity;
        if (movedQuantity == 0) {
            return movedFirst;
        }

        // Try move until we reach the max quantity, or we don't move anything anymore.
        while (movedQuantity < maxQuantity) {
            // Reduce size if remainder is less than the first moved quantity
            long toMoveQuantity = maxQuantity - movedQuantity;
            if (toMoveQuantity < movedFirstQuantity) {
                movedFirst = matcher.withQuantity(movedFirst, toMoveQuantity);
            }

            T moved = moveIngredients(source, destination, movedFirst, matchCondition, transaction);
            if (matcher.isEmpty(moved)) {
                break;
            }
            movedQuantity += matcher.getQuantity(moved);
        }

        return matcher.withQuantity(movedFirst, movedQuantity);
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
     * @throws InconsistentIngredientInsertionException When ingredients are lost due to inconsistent simulation.
     * @deprecated Use {@link #moveIngredientsIterative(IIngredientComponentStorage, IIngredientComponentStorage, Object, Object, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> T moveIngredientsIterative(IIngredientComponentStorage<T, M> source,
                                                    IIngredientComponentStorage<T, M> destination,
                                                    T instance, M matchCondition, boolean simulate)
            throws InconsistentIngredientInsertionException {
        if (simulate) {
            // When simulating, only do one iteration to avoid infinite loops
            try (var tx = Transaction.openRoot()) {
                return moveIngredients(source, destination, instance, matchCondition, tx);
            }
        }
        try (var tx = Transaction.openRoot()) {
            T result = moveIngredientsIterative(source, destination, instance, matchCondition, tx);
            tx.commit();
            return result;
        }
    }

    /**
     * Move the first instance that matches the given match condition from source to destination.
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param instance The prototype instance.
     * @param matchCondition The match condition.
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredients(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           T instance, M matchCondition, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();
        Iterator<T> it = source.iterator(instance, matcher.withoutCondition(matchCondition,
                source.getComponent().getPrimaryQuantifier().getMatchCondition()));
        if (source instanceof IngredientComponentStorageCollectionWrapper) {
            it = Lists.newArrayList(it).iterator();
        }
        M matchConditionExact;
        if (matcher.hasCondition(matchCondition, source.getComponent().getPrimaryQuantifier().getMatchCondition())) {
            matchConditionExact = matcher.getExactMatchCondition();
        } else {
            matchConditionExact = matcher.getExactMatchNoQuantityCondition();
        }
        long prototypeQuantity = matcher.getQuantity(instance);
        while (it.hasNext()) {
            T sourceInstance = it.next();
            if (matcher.getQuantity(sourceInstance) != prototypeQuantity) {
                sourceInstance = matcher.withQuantity(sourceInstance, prototypeQuantity);
            }
            T moved = moveIngredient(source, destination, sourceInstance, matchConditionExact, transaction);
            if (!matcher.isEmpty(moved)) {
                return moved;
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
     * @throws InconsistentIngredientInsertionException When ingredients are lost due to inconsistent simulation.
     * @deprecated Use {@link #moveIngredients(IIngredientComponentStorage, IIngredientComponentStorage, Object, Object, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> T moveIngredients(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           T instance, M matchCondition, boolean simulate)
            throws InconsistentIngredientInsertionException {
        try (var tx = Transaction.openRoot()) {
            T result = moveIngredients(source, destination, instance, matchCondition, tx);
            if (!simulate) tx.commit();
            return result;
        }
    }

    /**
     * Convert sources to an iterable.
     * This is to handle storages that are unsafe wrt to mutations of the underlying storage
     * while an iterator over it is being iterated.
     * @param source The source instance.
     * @return An iterable.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     */
    protected static <T, M> Iterable<T> storageToIterable(IIngredientComponentStorage<T, M> source) {
        if (source instanceof IngredientComponentStorageCollectionWrapper) {
            return Lists.newArrayList(source);
        } else {
            return source;
        }
    }

    /**
     * Move the first instance that matches the given predicate from source to destination.
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param predicate The predicate to match instances by.
     * @param maxQuantity The max quantity that can be moved.
     * @param exactQuantity If the max quantity should be interpreted as an exact quantity.
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredients(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           Predicate<T> predicate, long maxQuantity, boolean exactQuantity,
                                           TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();
        for (T sourceInstance : storageToIterable(source)) {
            if (predicate.test(sourceInstance)) {
                long extractQuantity = Math.min(maxQuantity, matcher.getQuantity(sourceInstance));
                if (exactQuantity && extractQuantity < maxQuantity) {
                    continue;
                }
                T toExtractPrototype = matcher.withQuantity(sourceInstance, extractQuantity);

                // Simulate extraction to know what can actually be extracted (rolled back).
                T simulatedExtracted;
                try (var simTx = Transaction.open(transaction)) {
                    simulatedExtracted = source.extract(toExtractPrototype,
                            matcher.getExactMatchNoQuantityCondition(), simTx);
                    // simTx not committed → rolled back
                }
                if (matcher.isEmpty(simulatedExtracted)) {
                    continue;
                }

                // In one real transaction: insert into destination, then extract only the accepted amount.
                try (var nestedTx = Transaction.open(transaction)) {
                    T remaining = destination.insert(simulatedExtracted, nestedTx);
                    long movedQuantity = matcher.getQuantity(simulatedExtracted) - matcher.getQuantity(remaining);
                    if (movedQuantity > 0 && (!exactQuantity || movedQuantity == maxQuantity)) {
                        T toExtract = matcher.withQuantity(simulatedExtracted, movedQuantity);
                        T actualExtracted = source.extract(toExtract, matcher.getExactMatchCondition(), nestedTx);
                        if (matcher.getQuantity(actualExtracted) == movedQuantity) {
                            nestedTx.commit();
                            return matcher.withQuantity(actualExtracted, movedQuantity);
                        }
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
     * @param maxQuantity The max quantity that can be moved.
     * @param exactQuantity If the max quantity should be interpreted as an exact quantity.
     * @param simulate If the movement should be simulated.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     * @throws InconsistentIngredientInsertionException When ingredients are lost due to inconsistent simulation.
     * @deprecated Use {@link #moveIngredients(IIngredientComponentStorage, IIngredientComponentStorage, Predicate, long, boolean, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> T moveIngredients(IIngredientComponentStorage<T, M> source,
                                           IIngredientComponentStorage<T, M> destination,
                                           Predicate<T> predicate, long maxQuantity, boolean exactQuantity,
                                           boolean simulate)
            throws InconsistentIngredientInsertionException {
        try (var tx = Transaction.openRoot()) {
            T result = moveIngredients(source, destination, predicate, maxQuantity, exactQuantity, tx);
            if (!simulate) tx.commit();
            return result;
        }
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
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredientsSlotted(IIngredientComponentStorage<T, M> source, int sourceSlot,
                                                  IIngredientComponentStorage<T, M> destination, int destinationSlot,
                                                  T instance, M matchCondition, TransactionContext transaction) {
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

            // Fail if a slot id is too large
            if (sourceSlot >= sourceSlotted.getSlots() || destinationSlot >= destinationSlotted.getSlots()) {
                return matcher.getEmptyInstance();
            }

            // Peek at source slot to check match before modifying
            long prototypeQuantity = matcher.getQuantity(instance);
            T sourceContents = sourceSlotted.getSlotContents(sourceSlot);
            if (!matcher.isEmpty(sourceContents)) {
                // Simulate extraction to check match and how much can be extracted (rolled back).
                T simulatedExtracted;
                try (var simTx = Transaction.open(transaction)) {
                    T simEx = sourceSlotted.extract(sourceSlot, prototypeQuantity, simTx);
                    if (matcher.isEmpty(simEx) || !matcher.matches(instance, simEx, matchCondition)) {
                        simulatedExtracted = matcher.getEmptyInstance();
                    } else {
                        simulatedExtracted = simEx;
                    }
                    // simTx not committed → rolled back
                }
                if (!matcher.isEmpty(simulatedExtracted)) {
                    boolean exactRequired = matcher.hasCondition(matchCondition,
                            source.getComponent().getPrimaryQuantifier().getMatchCondition());
                    try (var nestedTx = Transaction.open(transaction)) {
                        T remaining = destinationSlotted.insert(destinationSlot, simulatedExtracted, nestedTx);
                        long remainingQuantity = matcher.getQuantity(remaining);
                        long movedQuantity = matcher.getQuantity(simulatedExtracted) - remainingQuantity;
                        if (remainingQuantity == 0 || (movedQuantity > 0 && !exactRequired)) {
                            T actualExtracted = sourceSlotted.extract(sourceSlot, movedQuantity, nestedTx);
                            if (matcher.getQuantity(actualExtracted) == movedQuantity) {
                                nestedTx.commit();
                                return matcher.withQuantity(actualExtracted, movedQuantity);
                            }
                        }
                    }
                }
            }
        } else if (loopSourceSlots) {
            if (source instanceof IIngredientComponentStorageSlotted) {
                // Recursively call movement logic for each slot in the source if slotted.
                IIngredientComponentStorageSlotted<T, M> sourceSlotted = (IIngredientComponentStorageSlotted<T, M>) source;
                int slots = sourceSlotted.getSlots();
                for (int slot = 0; slot < slots; slot++) {
                    T moved = moveIngredientsSlotted(source, slot, destination, destinationSlot, instance, matchCondition, transaction);
                    if (!matcher.isEmpty(moved)) {
                        return moved;
                    }
                }
            } else {
                // If we don't have source slots, iterate over all source slot instances in a slotless way
                long prototypeQuantity = matcher.getQuantity(instance);
                if (loopDestinationSlots) {
                    // If exactQuantity is true, try to loop over destination slots manually,
                    // because our is-exact check is only done after insertion.
                    if (matcher.hasCondition(matchCondition, source.getComponent().getPrimaryQuantifier().getMatchCondition())
                            && destination instanceof IIngredientComponentStorageSlotted) {
                        IIngredientComponentStorageSlotted<T, M> destinationSlotted = (IIngredientComponentStorageSlotted<T, M>) destination;
                        int slots = destinationSlotted.getSlots();
                        for (int slot = 0; slot < slots; slot++) {
                            T moved = moveIngredientsSlotted(source, sourceSlot, destination, slot, instance, matchCondition, transaction);
                            if (!matcher.isEmpty(moved)) {
                                return moved;
                            }
                        }
                    } else {
                        return moveIngredients(source, destination, instance, matchCondition, transaction);
                    }
                } else {
                    if (!(destination instanceof IIngredientComponentStorageSlotted)) {
                        return matcher.getEmptyInstance();
                    }
                    IIngredientComponentStorageSlotted<T, M> destinationSlotted = (IIngredientComponentStorageSlotted<T, M>) destination;

                    // Fail if the destination slot id is too large
                    if (destinationSlot >= destinationSlotted.getSlots()) {
                        return matcher.getEmptyInstance();
                    }

                    for (T sourceInstance : storageToIterable(source)) {
                        if (matcher.matches(instance, sourceInstance, matcher.withoutCondition(matchCondition,
                                source.getComponent().getPrimaryQuantifier().getMatchCondition()))) {
                            if (matcher.getQuantity(sourceInstance) != prototypeQuantity) {
                                sourceInstance = matcher.withQuantity(sourceInstance, prototypeQuantity);
                            }
                            boolean exactRequired = matcher.hasCondition(matchCondition,
                                    source.getComponent().getPrimaryQuantifier().getMatchCondition());

                            // Simulate extraction to know what can be extracted (rolled back).
                            T simulatedExtracted;
                            try (var simTx = Transaction.open(transaction)) {
                                simulatedExtracted = source.extract(sourceInstance, matchCondition, simTx);
                                // simTx not committed → rolled back
                            }
                            if (matcher.isEmpty(simulatedExtracted)) {
                                continue;
                            }

                            // In one real transaction: insert into destination, then extract only the accepted amount.
                            try (var nestedTx = Transaction.open(transaction)) {
                                T remaining = destinationSlotted.insert(destinationSlot, simulatedExtracted, nestedTx);
                                long remainingQuantity = matcher.getQuantity(remaining);
                                long movedQuantity = matcher.getQuantity(simulatedExtracted) - remainingQuantity;
                                if (remainingQuantity == 0 || (movedQuantity > 0 && !exactRequired)) {
                                    T toExtract = matcher.withQuantity(simulatedExtracted, movedQuantity);
                                    T actualExtracted = source.extract(toExtract, matcher.getExactMatchCondition(), nestedTx);
                                    if (matcher.getQuantity(actualExtracted) == movedQuantity) {
                                        nestedTx.commit();
                                        return matcher.withQuantity(actualExtracted, movedQuantity);
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
                    T moved = moveIngredientsSlotted(source, sourceSlot, destination, slot, instance, matchCondition, transaction);
                    if (!matcher.isEmpty(moved)) {
                        return moved;
                    }
                }
            } else {
                // If we don't have destination slots, move from defined source slot
                IIngredientComponentStorageSlotted<T, M> sourceSlotted = (IIngredientComponentStorageSlotted<T, M>) source;

                // Fail if the source slot id is too large
                if (sourceSlot >= sourceSlotted.getSlots()) {
                    return matcher.getEmptyInstance();
                }

                long prototypeQuantity = matcher.getQuantity(instance);
                T sourceContents = sourceSlotted.getSlotContents(sourceSlot);
                if (!matcher.isEmpty(sourceContents)) {
                    boolean exactRequired = matcher.hasCondition(matchCondition,
                            source.getComponent().getPrimaryQuantifier().getMatchCondition());

                    // Simulate extraction to check match and how much can be extracted (rolled back).
                    T simulatedExtracted;
                    try (var simTx = Transaction.open(transaction)) {
                        T simEx = sourceSlotted.extract(sourceSlot, prototypeQuantity, simTx);
                        if (matcher.isEmpty(simEx) || !matcher.matches(instance, simEx, matchCondition)) {
                            simulatedExtracted = matcher.getEmptyInstance();
                        } else {
                            simulatedExtracted = simEx;
                        }
                        // simTx not committed → rolled back
                    }
                    if (!matcher.isEmpty(simulatedExtracted)) {
                        try (var nestedTx = Transaction.open(transaction)) {
                            T remaining = destination.insert(simulatedExtracted, nestedTx);
                            long remainingQuantity = matcher.getQuantity(remaining);
                            long movedQuantity = matcher.getQuantity(simulatedExtracted) - remainingQuantity;
                            if (movedQuantity > 0 && (!exactRequired || remainingQuantity == 0)) {
                                T actualExtracted = sourceSlotted.extract(sourceSlot, movedQuantity, nestedTx);
                                if (matcher.getQuantity(actualExtracted) == movedQuantity) {
                                    nestedTx.commit();
                                    return matcher.withQuantity(actualExtracted, movedQuantity);
                                }
                            }
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
     * @throws InconsistentIngredientInsertionException When ingredients are lost due to inconsistent simulation.
     * @deprecated Use {@link #moveIngredientsSlotted(IIngredientComponentStorage, int, IIngredientComponentStorage, int, Object, Object, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> T moveIngredientsSlotted(IIngredientComponentStorage<T, M> source, int sourceSlot,
                                                  IIngredientComponentStorage<T, M> destination, int destinationSlot,
                                                  T instance, M matchCondition, boolean simulate)
            throws InconsistentIngredientInsertionException {
        try (var tx = Transaction.openRoot()) {
            T result = moveIngredientsSlotted(source, sourceSlot, destination, destinationSlot, instance, matchCondition, tx);
            if (!simulate) tx.commit();
            return result;
        }
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
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredientsSlotted(IIngredientComponentStorage<T, M> source, int sourceSlot,
                                                  IIngredientComponentStorage<T, M> destination, int destinationSlot,
                                                  Predicate<T> predicate, long maxQuantity, boolean exactQuantity,
                                                  TransactionContext transaction) {
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

            // Fail if a slot id is too large
            if (sourceSlot >= sourceSlotted.getSlots() || destinationSlot >= destinationSlotted.getSlots()) {
                return matcher.getEmptyInstance();
            }

            // Check what's in source slot without modifying
            T sourceContents = sourceSlotted.getSlotContents(sourceSlot);
            if (!matcher.isEmpty(sourceContents) && predicate.test(sourceContents)) {
                long extractQuantity = Math.min(maxQuantity, matcher.getQuantity(sourceContents));
                if (!exactQuantity || extractQuantity == maxQuantity) {
                    // Simulate extraction to know how much can actually be extracted (rolled back).
                    T simulatedExtracted;
                    try (var simTx = Transaction.open(transaction)) {
                        simulatedExtracted = sourceSlotted.extract(sourceSlot, extractQuantity, simTx);
                        // simTx not committed → rolled back
                    }
                    if (!matcher.isEmpty(simulatedExtracted)) {
                        try (var nestedTx = Transaction.open(transaction)) {
                            T remaining = destinationSlotted.insert(destinationSlot, simulatedExtracted, nestedTx);
                            long remainingQuantity = matcher.getQuantity(remaining);
                            long movedQuantity = matcher.getQuantity(simulatedExtracted) - remainingQuantity;
                            if (remainingQuantity == 0 || (movedQuantity > 0 && !exactQuantity)) {
                                T actualExtracted = sourceSlotted.extract(sourceSlot, movedQuantity, nestedTx);
                                if (matcher.getQuantity(actualExtracted) == movedQuantity) {
                                    nestedTx.commit();
                                    return matcher.withQuantity(actualExtracted, movedQuantity);
                                }
                            }
                        }
                    }
                }
            }
        } else if (loopSourceSlots) {
            if (source instanceof IIngredientComponentStorageSlotted) {
                // Recursively call movement logic for each slot in the source if slotted.
                IIngredientComponentStorageSlotted<T, M> sourceSlotted = (IIngredientComponentStorageSlotted<T, M>) source;
                int slots = sourceSlotted.getSlots();
                for (int slot = 0; slot < slots; slot++) {
                    T moved = moveIngredientsSlotted(source, slot, destination, destinationSlot, predicate, maxQuantity, exactQuantity, transaction);
                    if (!matcher.isEmpty(moved)) {
                        return moved;
                    }
                }
            } else {
                // If we don't have source slots, iterate over all source slot instances in a slotless way
                if (loopDestinationSlots) {
                    // If exactQuantity is true, try to loop over destination slots manually,
                    // because our is-exact check is only done after insertion.
                    if (exactQuantity && destination instanceof IIngredientComponentStorageSlotted) {
                        // Recursively call movement logic for each slot in the destination if slotted.
                        IIngredientComponentStorageSlotted<T, M> destinationSlotted = (IIngredientComponentStorageSlotted<T, M>) destination;
                        int slots = destinationSlotted.getSlots();
                        for (int slot = 0; slot < slots; slot++) {
                            T moved = moveIngredientsSlotted(source, sourceSlot, destination, slot, predicate, maxQuantity, true, transaction);
                            if (!matcher.isEmpty(moved)) {
                                return moved;
                            }
                        }
                    } else {
                        return moveIngredients(source, destination, predicate, maxQuantity, exactQuantity, transaction);
                    }
                } else {
                    if (!(destination instanceof IIngredientComponentStorageSlotted)) {
                        return matcher.getEmptyInstance();
                    }
                    IIngredientComponentStorageSlotted<T, M> destinationSlotted = (IIngredientComponentStorageSlotted<T, M>) destination;

                    // Fail if the destination slot id is too large
                    if (destinationSlot >= destinationSlotted.getSlots()) {
                        return matcher.getEmptyInstance();
                    }

                    for (T sourceInstance : storageToIterable(source)) {
                        if (predicate.test(sourceInstance)) {
                            long extractQuantity = Math.min(maxQuantity, matcher.getQuantity(sourceInstance));
                            if (exactQuantity && extractQuantity < maxQuantity) {
                                continue;
                            }
                            T toExtractPrototype = matcher.withQuantity(sourceInstance, extractQuantity);

                            // Simulate extraction to know what can be extracted (rolled back).
                            T simulatedExtracted;
                            try (var simTx = Transaction.open(transaction)) {
                                simulatedExtracted = source.extract(toExtractPrototype,
                                        matcher.getExactMatchCondition(), simTx);
                                // simTx not committed → rolled back
                            }
                            if (matcher.isEmpty(simulatedExtracted)) {
                                continue;
                            }

                            // In one real transaction: insert into destination, then extract only the accepted amount.
                            try (var nestedTx = Transaction.open(transaction)) {
                                T remaining = destinationSlotted.insert(destinationSlot, simulatedExtracted, nestedTx);
                                long remainingQuantity = matcher.getQuantity(remaining);
                                long movedQuantity = matcher.getQuantity(simulatedExtracted) - remainingQuantity;
                                if (remainingQuantity == 0 || (movedQuantity > 0 && !exactQuantity)) {
                                    T toExtract = matcher.withQuantity(simulatedExtracted, movedQuantity);
                                    T actualExtracted = source.extract(toExtract, matcher.getExactMatchCondition(), nestedTx);
                                    if (matcher.getQuantity(actualExtracted) == movedQuantity) {
                                        nestedTx.commit();
                                        return matcher.withQuantity(actualExtracted, movedQuantity);
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
                    T moved = moveIngredientsSlotted(source, sourceSlot, destination, slot, predicate, maxQuantity, exactQuantity, transaction);
                    if (!matcher.isEmpty(moved)) {
                        return moved;
                    }
                }
            } else {
                // If we don't have destination slots, move from defined source slot
                IIngredientComponentStorageSlotted<T, M> sourceSlotted = (IIngredientComponentStorageSlotted<T, M>) source;

                // Fail if the source slot id is too large
                if (sourceSlot >= sourceSlotted.getSlots()) {
                    return matcher.getEmptyInstance();
                }

                T sourceContents = sourceSlotted.getSlotContents(sourceSlot);
                if (!matcher.isEmpty(sourceContents) && predicate.test(sourceContents)) {
                    long extractQuantity = Math.min(maxQuantity, matcher.getQuantity(sourceContents));
                    if (exactQuantity && extractQuantity < maxQuantity) {
                        return matcher.getEmptyInstance();
                    }

                    // Simulate extraction to know how much can actually be extracted (rolled back).
                    T simulatedExtracted;
                    try (var simTx = Transaction.open(transaction)) {
                        simulatedExtracted = sourceSlotted.extract(sourceSlot, extractQuantity, simTx);
                        // simTx not committed → rolled back
                    }
                    if (!matcher.isEmpty(simulatedExtracted)) {
                        try (var nestedTx = Transaction.open(transaction)) {
                            T remaining = destination.insert(simulatedExtracted, nestedTx);
                            long remainingQuantity = matcher.getQuantity(remaining);
                            long movedQuantity = matcher.getQuantity(simulatedExtracted) - remainingQuantity;
                            if (movedQuantity > 0 && (!exactQuantity || remainingQuantity == 0)) {
                                T actualExtracted = sourceSlotted.extract(sourceSlot, movedQuantity, nestedTx);
                                if (matcher.getQuantity(actualExtracted) == movedQuantity) {
                                    nestedTx.commit();
                                    return matcher.withQuantity(actualExtracted, movedQuantity);
                                }
                            }
                        }
                    }
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
     * @throws InconsistentIngredientInsertionException When ingredients are lost due to inconsistent simulation.
     * @deprecated Use {@link #moveIngredientsSlotted(IIngredientComponentStorage, int, IIngredientComponentStorage, int, Predicate, long, boolean, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> T moveIngredientsSlotted(IIngredientComponentStorage<T, M> source, int sourceSlot,
                                                  IIngredientComponentStorage<T, M> destination, int destinationSlot,
                                                  Predicate<T> predicate, long maxQuantity, boolean exactQuantity,
                                                  boolean simulate)
            throws InconsistentIngredientInsertionException {
        try (var tx = Transaction.openRoot()) {
            T result = moveIngredientsSlotted(source, sourceSlot, destination, destinationSlot, predicate, maxQuantity, exactQuantity, tx);
            if (!simulate) tx.commit();
            return result;
        }
    }

    /**
     * Move the first instance that matches the given match condition from source to destination.
     *
     * The main difference of this method to
     * {@link #moveIngredients(IIngredientComponentStorage, IIngredientComponentStorage, Object, Object, TransactionContext)}
     * is that the latter method will try checking *multiple* ingredients from the source,
     * while this method will only check the *first matching* ingredient.
     * This makes this method potentially more efficient than the latter.
     *
     * @param source A source storage to extract from.
     * @param destination A destination storage to insert to.
     * @param instance The prototype instance.
     * @param matchCondition The match condition.
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The moved ingredient.
     */
    public static <T, M> T moveIngredient(IIngredientComponentStorage<T, M> source,
                                          IIngredientComponentStorage<T, M> destination,
                                          T instance, M matchCondition, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = source.getComponent().getMatcher();
        boolean exactRequired = matcher.hasCondition(matchCondition,
                source.getComponent().getPrimaryQuantifier().getMatchCondition());

        // Simulate extraction to know what can be extracted (rolled back).
        T simulatedExtracted;
        try (var simTx = Transaction.open(transaction)) {
            simulatedExtracted = source.extract(instance, matchCondition, simTx);
            // simTx not committed → rolled back
        }
        if (matcher.isEmpty(simulatedExtracted)) {
            return matcher.getEmptyInstance();
        }

        // In one real transaction: insert into destination, then extract only the accepted amount.
        // This avoids having to re-insert remainder back into source (which may be extract-only).
        try (var nestedTx = Transaction.open(transaction)) {
            T remaining = destination.insert(simulatedExtracted, nestedTx);
            long movedQuantity = matcher.getQuantity(simulatedExtracted) - matcher.getQuantity(remaining);
            if (movedQuantity > 0) {
                if (exactRequired && movedQuantity != matcher.getQuantity(instance)) {
                    // Exact quantity required but destination couldn't accept it all - rollback
                    return matcher.getEmptyInstance();
                }
                T toExtract = matcher.withQuantity(simulatedExtracted, movedQuantity);
                T actualExtracted = source.extract(toExtract, matcher.getExactMatchCondition(), nestedTx);
                if (matcher.getQuantity(actualExtracted) == movedQuantity) {
                    nestedTx.commit();
                    return actualExtracted;
                }
            }
        }
        return matcher.getEmptyInstance();
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
     * @throws InconsistentIngredientInsertionException When ingredients are lost due to inconsistent simulation.
     * @deprecated Use {@link #moveIngredient(IIngredientComponentStorage, IIngredientComponentStorage, Object, Object, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> T moveIngredient(IIngredientComponentStorage<T, M> source,
                                          IIngredientComponentStorage<T, M> destination,
                                          T instance, M matchCondition, boolean simulate)
            throws InconsistentIngredientInsertionException {
        try (var tx = Transaction.openRoot()) {
            T result = moveIngredient(source, destination, instance, matchCondition, tx);
            if (!simulate) tx.commit();
            return result;
        }
    }

    /**
     * Insert an ingredient in a destination storage.
     *
     * The difference of this method compared to {@link IIngredientComponentStorage#insert(Object, TransactionContext)}
     * is that this method returns the actually inserted ingredient quantity
     * instead of the remaining ingredient that was not inserted.
     *
     * @param destination A storage.
     * @param instance An instance to insert.
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The actual inserted ingredient quantity in the transaction.
     */
    public static <T, M> long insertIngredientQuantity(IIngredientComponentStorage<T, M> destination,
                                                       T instance, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = destination.getComponent().getMatcher();
        long quantity = matcher.getQuantity(instance);
        if (quantity > 0) {
            T remainingInserted = destination.insert(instance, transaction);
            long remainingInsertedQuantity = matcher.getQuantity(remainingInserted);
            return quantity - remainingInsertedQuantity;
        }
        return 0;
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
     * @deprecated Use {@link #insertIngredientQuantity(IIngredientComponentStorage, Object, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> long insertIngredientQuantity(IIngredientComponentStorage<T, M> destination,
                                                       T instance, boolean simulate) {
        try (var tx = Transaction.openRoot()) {
            long result = insertIngredientQuantity(destination, instance, tx);
            if (!simulate) tx.commit();
            return result;
        }
    }

    /**
     * Insert an ingredient in a destination storage.
     *
     * The difference of this method compared to {@link IIngredientComponentStorage#insert(Object, TransactionContext)}
     * is that this method returns the actually inserted ingredient
     * instead of the remaining ingredient that was not inserted.
     *
     * @param destination A storage.
     * @param instance An instance to insert.
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The actual inserted ingredient in the transaction.
     */
    public static <T, M> T insertIngredient(IIngredientComponentStorage<T, M> destination,
                                            T instance, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = destination.getComponent().getMatcher();
        return matcher.withQuantity(instance, insertIngredientQuantity(destination, instance, transaction));
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
     * @deprecated Use {@link #insertIngredient(IIngredientComponentStorage, Object, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> T insertIngredient(IIngredientComponentStorage<T, M> destination,
                                            T instance, boolean simulate) {
        try (var tx = Transaction.openRoot()) {
            T result = insertIngredient(destination, instance, tx);
            if (!simulate) tx.commit();
            return result;
        }
    }

    /**
     * Insert an ingredient in a destination storage.
     * If the instance does not completely fit into the destination,
     * the remainder will be inserted into the source.
     *
     * The difference of this method compared to {@link IIngredientComponentStorage#insert(Object, TransactionContext)}
     * is that this method returns the actually inserted ingredient
     * instead of the remaining ingredient that was not inserted.
     *
     * @param source A source storage to insert fixup instances.
     * @param destination A storage.
     * @param instance An instance to insert.
     * @param transaction The transaction context.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The actual inserted ingredient in the transaction.
     */
    @Deprecated
    // TODO: remove in next major
    protected static <T, M> T insertIngredientRemainderFixup(IIngredientComponentStorage<T, M> source,
                                                          IIngredientComponentStorage<T, M> destination,
                                                          T instance, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = destination.getComponent().getMatcher();
        T remaining = destination.insert(instance, transaction);
        if (!matcher.isEmpty(remaining)) {
            source.insert(remaining, transaction);
        }
        return matcher.withQuantity(instance, matcher.getQuantity(instance) - matcher.getQuantity(remaining));
    }

    /**
     * Insert an ingredient in a destination storage.
     * If not in simulation mode, and the instance does not completely fit into the destination,
     * the remainder will be inserted into the source.
     * If not everything can be re-inserted into the source,
     * a warning will be emitted.
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
     * @throws InconsistentIngredientInsertionException When ingredients are lost due to inconsistent simulation.
     * @deprecated Use {@link #insertIngredientRemainderFixup(IIngredientComponentStorage, IIngredientComponentStorage, Object, TransactionContext)} instead.
     */
    @Deprecated
    // TODO: remove in next major
    public static <T, M> T insertIngredientRemainderFixup(IIngredientComponentStorage<T, M> source,
                                                          IIngredientComponentStorage<T, M> destination,
                                                          T instance, boolean simulate)
            throws InconsistentIngredientInsertionException {
        try (var tx = Transaction.openRoot()) {
            T result = insertIngredientRemainderFixup(source, destination, instance, tx);
            if (!simulate) tx.commit();
            return result;
        }
    }


    /**
     * Wrap the given ingredient storage,
     * where you can configure if the storage is allowed to be read, inserted to, or extracted from.
     *
     * This will distinguish between slotted and slotless storages.
     *
     * @param ingredientComponentStorage The storage to wrap.
     * @param read If the storage can be read.
     * @param insert If the storage can be inserted to.
     * @param extract If the storage can be extracted from.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return The wrapped storage.
     */
    public static <T, M> IIngredientComponentStorage<T, M> wrapStorage(IIngredientComponentStorage<T, M> ingredientComponentStorage,
                                                                       boolean read,
                                                                       boolean insert,
                                                                       boolean extract) {
        if (ingredientComponentStorage instanceof IIngredientComponentStorageSlotted) {
            return new IngredientComponentStorageSlottedWrapped<>(
                    (IIngredientComponentStorageSlotted<T, M>) ingredientComponentStorage, read, insert, extract);
        } else {
            return new IngredientComponentStorageWrapped<>(ingredientComponentStorage, read, insert, extract);
        }
    }

    /**
     * Serialize the given storage to NBT.
     * <p>
     * All ingredients, the max quantity, and whether or not it is slotted will be stored.
     *
     * @param <T>         The instance type.
     * @param <M>         The matching condition parameter.
     * @param valueOutput The holder lookup provider.
     * @param storage     An ingredient storage.
     */
    public static <T, M> void serialize(ValueOutput valueOutput, IIngredientComponentStorage<T, M> storage) {
        IngredientCollections.serialize(valueOutput, new IngredientArrayList<>(storage.getComponent(), storage.iterator()));
        valueOutput.putLong("maxQuantity", storage.getMaxQuantity());
        valueOutput.putBoolean("slotted", storage instanceof IIngredientComponentStorageSlotted);
    }

    /**
     * Deserialize the storage from the given NBT tag.
     * <p>
     * All ingredients, the max quantity, and whether or not it is slotted will be restored.
     *
     * @param valueInput An NBT tag.
     * @param rateLimit  The rate limit per insertion/extraction.
     * @return The deserialized storage.
     * @throws IllegalArgumentException If the tag was invalid.
     */
    public static IIngredientComponentStorage<?, ?> deserialize(ValueInput valueInput, long rateLimit) {
        long maxQuantity = valueInput.getLong("maxQuantity").orElseThrow(() -> new IllegalArgumentException("No maxQuantity was found in the given tag"));
        if (valueInput.getBooleanOr("slotted", false)) {
            return new IngredientComponentStorageSlottedCollectionWrapper<>(
                    IngredientCollections.deserialize(valueInput, IngredientArrayList::new), maxQuantity, rateLimit);
        } else {
            return new IngredientComponentStorageCollectionWrapper<>(
                    IngredientCollections.deserialize(valueInput, (IngredientCollections.IIngredientCollectionConstructor<IIngredientCollapsedCollectionMutable<?, ?>>) IngredientCollectionHelpers::createCollapsedCollection),
                    maxQuantity, rateLimit);
        }
    }

    /**
     * Helper interface for constructing an {@link IIngredientCollection} based on an {@link IngredientComponent}.
     * @param <C> The storage type.
     */
    public static interface IIngredientStorageConstructor<C extends IIngredientComponentStorage<?, ?>> {
        public <T, M> C create(IngredientComponent<T, M> ingredientComponent);
    }

}
