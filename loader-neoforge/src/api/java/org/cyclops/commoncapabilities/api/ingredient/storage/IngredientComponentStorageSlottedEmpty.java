package org.cyclops.commoncapabilities.api.ingredient.storage;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nonnull;

/**
 * A dummy slotted ingredient component storage that is empty.
 * @author rubensworks
 */
public class IngredientComponentStorageSlottedEmpty<T, M> extends IngredientComponentStorageEmpty<T, M>
        implements IIngredientComponentStorageSlotted<T, M> {

    public IngredientComponentStorageSlottedEmpty(IngredientComponent<T, M> component) {
        super(component);
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public T getSlotContents(int slot) {
        return getComponent().getMatcher().getEmptyInstance();
    }

    @Override
    public long getMaxQuantity(int slot) {
        return 0;
    }

    @Override
    public T insert(int slot, @Nonnull T ingredient, boolean simulate) {
        return ingredient;
    }

    @Override
    public T extract(int slot, long maxQuantity, boolean simulate) {
        return getComponent().getMatcher().getEmptyInstance();
    }
}
