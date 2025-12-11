package org.cyclops.cyclopscore.ingredient.storage;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientListMutable;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * An implementation of {@link IIngredientComponentStorageSlotted}
 * that internally uses a {@link IIngredientListMutable} to store instances.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientComponentStorageSlottedCollectionWrapper<T, M> implements IIngredientComponentStorageSlotted<T, M> {

    private final IIngredientListMutable<T, M> ingredientCollection;
    private final long maxSlotQuantity;
    private final long rateLimit;
    private final Int2ObjectMap<SlotJournal> snapshotJournals;

    private long quantity;

    public IngredientComponentStorageSlottedCollectionWrapper(IIngredientListMutable<T, M> ingredientCollection,
                                                              long maxSlotQuantity, long rateLimit) {
        this.ingredientCollection = ingredientCollection;
        this.maxSlotQuantity = maxSlotQuantity;
        this.rateLimit = rateLimit;
        this.snapshotJournals = new Int2ObjectOpenHashMap<>();

        this.quantity = 0;
    }

    @Override
    public int getSlots() {
        return ingredientCollection.size();
    }

    @Override
    public T getSlotContents(int slot) {
        return ingredientCollection.get(slot);
    }

    @Override
    public long getMaxQuantity(int slot) {
        return this.maxSlotQuantity;
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

    SlotJournal getSnapshotJournal(int slot) {
        SlotJournal snapshotJournal = snapshotJournals.get(slot);
        if (snapshotJournal == null) {
            snapshotJournal = new SlotJournal(slot);
            snapshotJournals.put(slot, snapshotJournal);
        }
        return snapshotJournal;
    }

    @Override
    public T insert(int slot, @Nonnull T ingredient, TransactionContext transaction) {
        T insertingIngredient = rateLimit(ingredient, getMaxQuantity() - this.quantity);
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        if (!matcher.isEmpty(insertingIngredient)) {
            T contained = ingredientCollection.get(slot);
            if (matcher.isEmpty(contained)
                    || matcher.matches(ingredient, contained, matcher.getExactMatchNoQuantityCondition())) {
                long addQuantity = Math.min(getMaxQuantity(slot) - matcher.getQuantity(contained),
                        matcher.getQuantity(insertingIngredient));
                getSnapshotJournal(slot).updateSnapshots(transaction);
                ingredientCollection.set(slot, matcher.withQuantity(ingredient,
                        matcher.getQuantity(contained) + addQuantity));
                this.quantity += addQuantity;
                return matcher.withQuantity(insertingIngredient, matcher.getQuantity(ingredient) - addQuantity);
            }
        }
        return ingredient;
    }

    @Override
    public T extract(int slot, long maxQuantity, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        T contained = ingredientCollection.get(slot);
        if (!matcher.isEmpty(contained)) {
            T extractingIngredient = rateLimit(contained, maxQuantity);
            getSnapshotJournal(slot).updateSnapshots(transaction);
            long removeQuantity = matcher.getQuantity(extractingIngredient);
            ingredientCollection.set(slot, matcher.withQuantity(contained,
                    matcher.getQuantity(contained) - removeQuantity));
            this.quantity -= removeQuantity;
            return extractingIngredient;
        }
        return matcher.getEmptyInstance();
    }

    @Override
    public IngredientComponent<T, M> getComponent() {
        return ingredientCollection.getComponent();
    }

    @Override
    public Iterator<T> iterator() {
        return ingredientCollection.iterator();
    }

    @Override
    public Iterator<T> iterator(@Nonnull T prototype, M matchCondition) {
        return ingredientCollection.iterator(prototype, matchCondition);
    }

    @Override
    public long getMaxQuantity() {
        return this.getSlots() * this.maxSlotQuantity;
    }

    @Override
    public T insert(@Nonnull T ingredient, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        long givenQuantity = matcher.getQuantity(ingredient);
        for (int slot = 0; slot < getSlots(); slot++) {
            getSnapshotJournal(slot).updateSnapshots(transaction);
            try (var tx = Transaction.open(transaction)) {
                T insertRemaining = this.insert(slot, ingredient, tx);
                if (matcher.getQuantity(insertRemaining) != givenQuantity) {
                    tx.commit();
                    return insertRemaining;
                }
            }
        }
        return ingredient;
    }

    @Override
    public T extract(@Nonnull T prototype, M matchCondition, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        for (int slot = 0; slot < getSlots(); slot++) {
            T contained = ingredientCollection.get(slot);
            if (!matcher.isEmpty(contained) && matcher.matches(contained, prototype, matchCondition)) {
                T extractingIngredient = rateLimit(contained, matcher.getQuantity(prototype));
                if (matcher.matches(prototype, extractingIngredient, matchCondition)) {
                    getSnapshotJournal(slot).updateSnapshots(transaction);
                    long removeQuantity = matcher.getQuantity(extractingIngredient);
                    ingredientCollection.set(slot, matcher.withQuantity(contained,
                            matcher.getQuantity(contained) - removeQuantity));
                    this.quantity -= removeQuantity;
                    return extractingIngredient;
                }
            }
        }
        return matcher.getEmptyInstance();
    }

    @Override
    public T extract(long maxQuantity, TransactionContext transaction) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        for (int slot = 0; slot < getSlots(); slot++) {
            getSnapshotJournal(slot).updateSnapshots(transaction);
            try (var tx = Transaction.open(transaction)) {
                T extracted = this.extract(slot, maxQuantity, tx);
                if (!matcher.isEmpty(extracted)) {
                    tx.commit();
                    return extracted;
                }
            }
        }
        return matcher.getEmptyInstance();
    }

    private class SlotJournal extends SnapshotJournal<T> {
        private final int index;

        private SlotJournal(int index) {
            this.index = index;
        }

        @Override
        protected T createSnapshot() {
            return getComponent().getMatcher().copy(ingredientCollection.get(index));
        }

        @Override
        protected void revertToSnapshot(T snapshot) {
            T oldStack = ingredientCollection.get(index);
            ingredientCollection.set(index, snapshot);

            // Fix quantity
            long oldQuantity = getComponent().getMatcher().getQuantity(oldStack);
            quantity += getComponent().getMatcher().getQuantity(snapshot) - oldQuantity;
        }

        @Override
        protected void onRootCommit(T originalState) {
            super.onRootCommit(originalState);
            // Clear journal to avoid memory leaks
            snapshotJournals.remove(index);
        }
    }
}
