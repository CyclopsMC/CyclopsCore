package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Iterators;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

/**
 * A composite ingredient component storage
 * @param <T> The instance type.
 * @param <M> The matching condition parameter, may be Void. Instances MUST properly implement the equals method.
 * @author rubensworks
 */
public class IngredientComponentStorageComposite<T, M> implements IIngredientComponentStorage<T, M> {

    protected final IngredientComponent<T, M> ingredientComponent;
    protected final Collection<IIngredientComponentStorage<T, M>> storages;

    public IngredientComponentStorageComposite(IngredientComponent<T, M> ingredientComponent, Collection<IIngredientComponentStorage<T, M>> storages) {
        this.ingredientComponent = ingredientComponent;
        this.storages = storages;
    }

    @Override
    public IngredientComponent<T, M> getComponent() {
        return this.ingredientComponent;
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.concat(Iterators.transform(this.storages.iterator(), IIngredientComponentStorage::iterator));
    }

    @Override
    public Iterator<T> iterator(@NotNull T prototype, M matchCondition) {
        return Iterators.concat(Iterators.transform(this.storages.iterator(), i -> i.iterator(prototype, matchCondition)));
    }

    @Override
    public long getMaxQuantity() {
        long sum = 0;
        Iterator<IIngredientComponentStorage<T, M>> it = this.storages.iterator();
        while (it.hasNext() && sum < Long.MAX_VALUE) {
            try {
                sum = Math.addExact(sum, it.next().getMaxQuantity());
            } catch (ArithmeticException e) {
                sum = Long.MAX_VALUE; // If we had an overflow, we're already at max quantity.
            }
        }
        return sum;
    }

    @Override
    public T insert(@NotNull T ingredient, boolean simulate) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        for (IIngredientComponentStorage<T, M> storage : this.storages) {
            ingredient = storage.insert(ingredient, simulate);
            if (matcher.isEmpty(ingredient)) {
                break;
            }
        }
        return ingredient;
    }

    @Override
    public T extract(@NotNull T prototype, M matchCondition, boolean simulate) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        for (IIngredientComponentStorage<T, M> storage : this.storages) {
            T extracted = storage.extract(prototype, matchCondition, simulate);
            if (!matcher.isEmpty(extracted)) {
                return extracted;
            }
        }
        return matcher.getEmptyInstance();
    }

    @Override
    public T extract(long maxQuantity, boolean simulate) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        for (IIngredientComponentStorage<T, M> storage : this.storages) {
            T extracted = storage.extract(maxQuantity, simulate);
            if (!matcher.isEmpty(extracted)) {
                return extracted;
            }
        }
        return matcher.getEmptyInstance();
    }
}
