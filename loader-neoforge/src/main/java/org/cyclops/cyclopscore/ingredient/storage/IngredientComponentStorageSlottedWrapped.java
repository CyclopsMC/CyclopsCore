package org.cyclops.cyclopscore.ingredient.storage;

import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;

import javax.annotation.Nonnull;

/**
 * An ingredient storage wrapper.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientComponentStorageSlottedWrapped<T, M> extends IngredientComponentStorageWrapped<T, M>
        implements IIngredientComponentStorageSlotted<T, M> {

    private final IIngredientComponentStorageSlotted<T, M> ingredientComponentStorageSlotted;

    public IngredientComponentStorageSlottedWrapped(IIngredientComponentStorageSlotted<T, M> ingredientComponentStorageSlotted,
                                                    boolean iterate, boolean insert, boolean extract) {
        super(ingredientComponentStorageSlotted, iterate, insert, extract);
        this.ingredientComponentStorageSlotted = ingredientComponentStorageSlotted;
    }

    @Override
    protected IIngredientComponentStorageSlotted<T, M> getIngredientComponentStorage() {
        return ingredientComponentStorageSlotted;
    }

    @Override
    public int getSlots() {
        if (!isRead()) {
            return 0;
        }
        return getIngredientComponentStorage().getSlots();
    }

    @Override
    public T getSlotContents(int slot) {
        if (!isRead()) {
            return getComponent().getMatcher().getEmptyInstance();
        }
        return getIngredientComponentStorage().getSlotContents(slot);
    }

    @Override
    public long getMaxQuantity(int slot) {
        if (!isRead()) {
            return 0;
        }
        return getIngredientComponentStorage().getMaxQuantity(slot);
    }

    @Override
    public T insert(int slot, @Nonnull T ingredient, boolean simulate) {
        if (!isInsert()) {
            return ingredient;
        }
        return getIngredientComponentStorage().insert(slot, ingredient, simulate);
    }

    @Override
    public T extract(int slot, long maxQuantity, boolean simulate) {
        if (!isExtract()) {
            return getComponent().getMatcher().getEmptyInstance();
        }
        return getIngredientComponentStorage().extract(slot, maxQuantity, simulate);
    }
}
