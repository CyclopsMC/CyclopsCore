package org.cyclops.cyclopscore.ingredient.storage;

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

    private long quantity;

    public IngredientComponentStorageSlottedCollectionWrapper(IIngredientListMutable<T, M> ingredientCollection,
                                                              long maxSlotQuantity, long rateLimit) {
        this.ingredientCollection = ingredientCollection;
        this.maxSlotQuantity = maxSlotQuantity;
        this.rateLimit = rateLimit;

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

    @Override
    public T insert(int slot, @Nonnull T ingredient, boolean simulate) {
        T insertingIngredient = rateLimit(ingredient, getMaxQuantity() - this.quantity);
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        if (!matcher.isEmpty(insertingIngredient)) {
            T contained = ingredientCollection.get(slot);
            if (matcher.isEmpty(contained)
                    || matcher.matches(ingredient, contained, matcher.getExactMatchNoQuantityCondition())) {
                long addQuantity = Math.min(getMaxQuantity(slot) - matcher.getQuantity(contained),
                        matcher.getQuantity(insertingIngredient));
                if (!simulate) {
                    ingredientCollection.set(slot, matcher.withQuantity(ingredient,
                            matcher.getQuantity(contained) + addQuantity));
                    this.quantity += addQuantity;
                }
                return matcher.withQuantity(insertingIngredient, matcher.getQuantity(ingredient) - addQuantity);
            }
        }
        return ingredient;
    }

    @Override
    public T extract(int slot, long maxQuantity, boolean simulate) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        T contained = ingredientCollection.get(slot);
        if (!matcher.isEmpty(contained)) {
            T extractingIngredient = rateLimit(contained, maxQuantity);
            if (!simulate) {
                long removeQuantity = matcher.getQuantity(extractingIngredient);
                ingredientCollection.set(slot, matcher.withQuantity(contained,
                        matcher.getQuantity(contained) - removeQuantity));
                this.quantity -= removeQuantity;
            }
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
    public T insert(@Nonnull T ingredient, boolean simulate) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        long givenQuantity = matcher.getQuantity(ingredient);
        for (int slot = 0; slot < getSlots(); slot++) {
            T insertRemaining = this.insert(slot, ingredient, true);
            if (matcher.getQuantity(insertRemaining) != givenQuantity) {
                return simulate ? insertRemaining : this.insert(slot, ingredient, false);
            }
        }
        return ingredient;
    }

    @Override
    public T extract(@Nonnull T prototype, M matchCondition, boolean simulate) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        for (int slot = 0; slot < getSlots(); slot++) {
            T contained = ingredientCollection.get(slot);
            if (!matcher.isEmpty(contained) && matcher.matches(contained, prototype, matchCondition)) {
                T extractingIngredient = rateLimit(contained, matcher.getQuantity(prototype));
                if (matcher.matches(prototype, extractingIngredient, matchCondition)) {
                    if (!simulate) {
                        long removeQuantity = matcher.getQuantity(extractingIngredient);
                        ingredientCollection.set(slot, matcher.withQuantity(contained,
                                matcher.getQuantity(contained) - removeQuantity));
                        this.quantity -= removeQuantity;
                    }
                    return extractingIngredient;
                }
            }
        }
        return matcher.getEmptyInstance();
    }

    @Override
    public T extract(long maxQuantity, boolean simulate) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        for (int slot = 0; slot < getSlots(); slot++) {
            T extracted = this.extract(slot, maxQuantity, true);
            if (!matcher.isEmpty(extracted)) {
                return simulate ? extracted : this.extract(slot, maxQuantity, false);
            }
        }
        return matcher.getEmptyInstance();
    }
}
