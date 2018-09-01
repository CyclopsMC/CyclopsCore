package org.cyclops.cyclopscore.ingredient.storage;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollapsedCollectionMutable;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * An implementation of {@link IIngredientComponentStorage}
 * that internally uses a {@link IIngredientCollapsedCollectionMutable} to store instances.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientComponentStorageCollectionWrapper<T, M> implements IIngredientComponentStorage<T, M> {

    private final IIngredientCollapsedCollectionMutable<T, M> ingredientCollection;
    private final long maxQuantity;
    private final long rateLimit;

    private long quantity;

    public IngredientComponentStorageCollectionWrapper(IIngredientCollapsedCollectionMutable<T, M> ingredientCollection) {
        this(ingredientCollection, Long.MAX_VALUE, Long.MAX_VALUE);
    }

    public IngredientComponentStorageCollectionWrapper(IIngredientCollapsedCollectionMutable<T, M> ingredientCollection,
                                                       long maxQuantity, long rateLimit) {
        this.ingredientCollection = ingredientCollection;
        this.maxQuantity = maxQuantity;
        this.rateLimit = rateLimit;

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

    @Override
    public T insert(@Nonnull T ingredient, boolean simulate) {
        T insertingIngredient = rateLimit(ingredient, getMaxQuantity() - this.quantity);
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        if (!matcher.isEmpty(insertingIngredient)) {
            boolean added = this.ingredientCollection.add(insertingIngredient);
            if (added) {
                if (simulate) {
                    this.ingredientCollection.remove(insertingIngredient);
                } else {
                    this.quantity += matcher.getQuantity(insertingIngredient);
                }
                return matcher.withQuantity(insertingIngredient, matcher.getQuantity(ingredient) - matcher.getQuantity(insertingIngredient));
            }
        }
        return ingredient;
    }

    @Override
    public T extract(@Nonnull T prototype, M matchCondition, boolean simulate) {
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

        if (!simulate) {
            this.ingredientCollection.remove(toExtract);
            this.quantity -= matcher.getQuantity(toExtract);
        }
        return toExtract;
    }

    @Override
    public T extract(long maxQuantity, boolean simulate) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        T toExtract = matcher.getEmptyInstance();
        for (T t : this.ingredientCollection) {
            if (!matcher.isEmpty(t)) {
                toExtract = t;
                break;
            }
        }
        toExtract = this.rateLimit(toExtract, maxQuantity);

        if (!simulate) {
            this.ingredientCollection.remove(toExtract);
            this.quantity -= getComponent().getMatcher().getQuantity(toExtract);
        }

        return toExtract;
    }

    long getQuantity() {
        return quantity;
    }

    void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
