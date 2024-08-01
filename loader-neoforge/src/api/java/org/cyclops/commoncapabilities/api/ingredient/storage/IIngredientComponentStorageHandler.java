package org.cyclops.commoncapabilities.api.ingredient.storage;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * A capability that can hold ingredient component storages.
 *
 * A capability separate from {@link IIngredientComponentStorage}
 * is needed because multiple storages could be exposed by a single capability provider.
 *
 * @author rubensworks
 */
public interface IIngredientComponentStorageHandler {

    /**
     * @return The ingredient component types this handler holds.
     */
    public Collection<IngredientComponent<?, ?>> getComponents();

    /**
     * Get the storage for the given ingredient component,
     * @param ingredientComponent The ingredient component.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter, may be Void. Instances MUST properly implement the equals method.
     * @return The ingredient component storage, or null if it is not available.
     */
    @Nullable
    public <T, M> IIngredientComponentStorage<T, M> getStorage(IngredientComponent<T, M> ingredientComponent);

}
