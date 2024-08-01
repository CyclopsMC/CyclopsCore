package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Iterators;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * An ingredient storage wrapper.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientComponentStorageWrapped<T, M> implements IIngredientComponentStorage<T, M> {

    private final IIngredientComponentStorage<T, M> ingredientComponentStorage;
    private final boolean read;
    private final boolean insert;
    private final boolean extract;

    public IngredientComponentStorageWrapped(IIngredientComponentStorage<T, M> ingredientComponentStorage,
                                             boolean read, boolean insert, boolean extract) {
        this.ingredientComponentStorage = ingredientComponentStorage;
        this.read = read;
        this.insert = insert;
        this.extract = extract;
    }

    protected IIngredientComponentStorage<T, M> getIngredientComponentStorage() {
        return ingredientComponentStorage;
    }

    protected boolean isRead() {
        return read;
    }

    protected boolean isInsert() {
        return insert;
    }

    protected boolean isExtract() {
        return extract;
    }

    @Override
    public IngredientComponent<T, M> getComponent() {
        return getIngredientComponentStorage().getComponent();
    }

    @Override
    public Iterator<T> iterator() {
        if (!isRead()) {
            return Iterators.forArray();
        }
        return getIngredientComponentStorage().iterator();
    }

    @Override
    public Iterator<T> iterator(@Nonnull T prototype, M matchCondition) {
        if (!isRead()) {
            return Iterators.forArray();
        }
        return getIngredientComponentStorage().iterator(prototype, matchCondition);
    }

    @Override
    public long getMaxQuantity() {
        if (!isRead()) {
            return 0;
        }
        return getIngredientComponentStorage().getMaxQuantity();
    }

    @Override
    public T insert(@Nonnull T ingredient, boolean simulate) {
        if (!isInsert()) {
            return ingredient;
        }
        return getIngredientComponentStorage().insert(ingredient, simulate);
    }

    @Override
    public T extract(@Nonnull T prototype, M matchCondition, boolean simulate) {
        if (!isExtract()) {
            return getComponent().getMatcher().getEmptyInstance();
        }
        return getIngredientComponentStorage().extract(prototype, matchCondition, simulate);
    }

    @Override
    public T extract(long maxQuantity, boolean simulate) {
        if (!isExtract()) {
            return getComponent().getMatcher().getEmptyInstance();
        }
        return getIngredientComponentStorage().extract(maxQuantity, simulate);
    }
}
