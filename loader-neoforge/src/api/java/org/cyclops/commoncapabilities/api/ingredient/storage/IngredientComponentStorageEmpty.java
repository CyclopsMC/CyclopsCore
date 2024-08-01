package org.cyclops.commoncapabilities.api.ingredient.storage;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;

/**
 * A dummy ingredient component storage that is empty.
 * @author rubensworks
 */
public class IngredientComponentStorageEmpty<T, M> implements IIngredientComponentStorage<T, M> {

    private final IngredientComponent<T, M> component;

    public IngredientComponentStorageEmpty(IngredientComponent<T, M> component) {
        this.component = component;
    }

    @Override
    public IngredientComponent<T, M> getComponent() {
        return component;
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public Iterator<T> iterator(@Nonnull T prototype, M matchFlags) {
        return Collections.emptyIterator();
    }

    @Override
    public long getMaxQuantity() {
        return 0;
    }

    @Nonnull
    @Override
    public T insert(@Nonnull T ingredient, boolean simulate) {
        return ingredient;
    }

    @Nonnull
    @Override
    public T extract(@Nonnull T prototype, M matchFlags, boolean simulate) {
        return getComponent().getMatcher().getEmptyInstance();
    }

    @Override
    public T extract(long maxQuantity, boolean simulate) {
        return getComponent().getMatcher().getEmptyInstance();
    }
}
