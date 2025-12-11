package org.cyclops.cyclopscore.ingredient.storage;

import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollapsedCollectionMutable;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientMapMutable;
import org.cyclops.cyclopscore.ingredient.collection.IngredientHashMap;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * An implementation of {@link IIngredientComponentStorage}
 * that internally uses a {@link IIngredientCollapsedCollectionMutable} to store instances.
 *
 * WARNING: This class does not allow mutations of the underlying storage while an iterator over it is being iterated.
 *
 * WARNING: Repeated transaction-based calls that are reverted every time, are not guaranteed to always produce the same
 * results. This is because the order of ingredients can be mutated after transaction reversal.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientComponentStorageCollectionWrapper<T, M> implements IIngredientComponentStorage<T, M> {

    private final IIngredientCollapsedCollectionMutable<T, M> ingredientCollection;
    private final long maxQuantity;
    private final long rateLimit;
    private final IIngredientMapMutable<T, M, PrototypeInstanceJournal> snapshotJournals;

    private long quantity;

    public IngredientComponentStorageCollectionWrapper(IIngredientCollapsedCollectionMutable<T, M> ingredientCollection) {
        this(ingredientCollection, Long.MAX_VALUE, Long.MAX_VALUE);
    }

    public IngredientComponentStorageCollectionWrapper(IIngredientCollapsedCollectionMutable<T, M> ingredientCollection,
                                                       long maxQuantity, long rateLimit) {
        this.ingredientCollection = ingredientCollection;
        this.maxQuantity = maxQuantity;
        this.rateLimit = rateLimit;
        this.snapshotJournals = new IngredientHashMap<>(ingredientCollection.getComponent());

        this.quantity = 0;
    }

    @Override
    public IngredientComponent<T, M> getComponent() {
        return this.ingredientCollection.getComponent();
    }

    @Override
    public Iterator<T> iterator() {
        return this.ingredientCollection.iterator();
    }

    @Override
    public Iterator<T> iterator(@Nonnull T prototype, M matchCondition) {
        return this.ingredientCollection.iterator(prototype, matchCondition);
    }

    @Override
    public long getMaxQuantity() {
        return this.maxQuantity;
    }

    T rateLimit(T instance, long allowedQuantity) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        long quantity = matcher.getQuantity(instance);
        long actualQuantity = Math.min(quantity, Math.min(allowedQuantity, this.rateLimit));
        if (actualQuantity == quantity) {
            return instance;
        }
        return matcher.withQuantity(instance, actualQuantity);
    }

    PrototypeInstanceJournal getSnapshotJournal(T ingredient) {
        ingredient = getComponent().getMatcher().withQuantity(ingredient, 1); // Convert to prototype
        PrototypeInstanceJournal snapshotJournal = snapshotJournals.get(ingredient);
        if (snapshotJournal == null) {
            snapshotJournal = new PrototypeInstanceJournal(ingredient);
            snapshotJournals.put(ingredient, snapshotJournal);
        }
        return snapshotJournal;
    }

    @Override
    public T insert(@Nonnull T ingredient, TransactionContext transaction) {
        T insertingIngredient = rateLimit(ingredient, getMaxQuantity() - this.quantity);
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        if (!matcher.isEmpty(insertingIngredient)) {
            getSnapshotJournal(ingredient).updateSnapshots(transaction);
            boolean added = this.ingredientCollection.add(insertingIngredient);
            if (added) {
                this.quantity += matcher.getQuantity(insertingIngredient);
                return matcher.withQuantity(insertingIngredient, matcher.getQuantity(ingredient) - matcher.getQuantity(insertingIngredient));
            }
        }
        return ingredient;
    }

    @Override
    public T extract(@Nonnull T prototype, M matchCondition, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        T toExtract = matcher.getEmptyInstance();
        Iterator<T> it = this.ingredientCollection.iterator(prototype, matcher.withoutCondition(matchCondition,
                getComponent().getPrimaryQuantifier().getMatchCondition()));
        while (it.hasNext()) {
            T t = it.next();
            if (!matcher.isEmpty(t)) {
                toExtract = t;
                break;
            }
        }
        toExtract = this.rateLimit(toExtract, matcher.getQuantity(prototype));

        if (!matcher.matches(prototype, toExtract, matchCondition)) {
            return getComponent().getMatcher().getEmptyInstance();
        }

        getSnapshotJournal(toExtract).updateSnapshots(transaction);
        this.ingredientCollection.remove(toExtract);
        this.quantity -= matcher.getQuantity(toExtract);
        return toExtract;
    }

    @Override
    public T extract(long maxQuantity, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        T toExtract = matcher.getEmptyInstance();
        for (T t : this.ingredientCollection) {
            if (!matcher.isEmpty(t)) {
                toExtract = t;
                break;
            }
        }
        toExtract = this.rateLimit(toExtract, maxQuantity);

        getSnapshotJournal(toExtract).updateSnapshots(transaction);
        this.ingredientCollection.remove(toExtract);
        this.quantity -= getComponent().getMatcher().getQuantity(toExtract);

        return toExtract;
    }

    long getQuantity() {
        return quantity;
    }

    void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    class PrototypeInstanceJournal extends SnapshotJournal<Long> {
        private final T prototype;

        private PrototypeInstanceJournal(T prototype) {
            this.prototype = prototype;
        }

        @Override
        protected Long createSnapshot() {
            return ingredientCollection.getQuantity(prototype);
        }

        @Override
        protected void revertToSnapshot(Long snapshot) {
            Long oldQuantity = ingredientCollection.getQuantity(prototype);
            ingredientCollection.setQuantity(prototype, snapshot);

            // Fix quantity
            quantity += snapshot - oldQuantity;
        }

        @Override
        protected void onRootCommit(Long originalState) {
            super.onRootCommit(originalState);
            // Clear journal to avoid memory leaks
            snapshotJournals.remove(prototype);
        }
    }
}
