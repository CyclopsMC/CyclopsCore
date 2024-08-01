package org.cyclops.commoncapabilities.api.ingredient.storage;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.capability.ICapabilityGetter;

import java.util.Optional;

/**
 * A handler for wrapping external storage interfaces into {@link IIngredientComponentStorage}
 * and the other way ({@link IIngredientComponentStorage} into external storage interfaces).
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter, may be Void. Instances MUST properly implement the equals method.
 * @param <S> The external storage type.
 * @author rubensworks
 */
public interface IIngredientComponentStorageWrapperHandler<T, M, S, C> {

    /**
     * @return The ingredient component.
     */
    public IngredientComponent<T, M> getComponent();

    /**
     * Wrap the given storage.
     * @param storage The external storage to wrap.
     * @return A component storage.
     */
    public IIngredientComponentStorage<T, M> wrapComponentStorage(S storage);

    /**
     * Wrap the given storage.
     * @param componentStorage The component storage to wrap.
     * @return A component storage.
     */
    public S wrapStorage(IIngredientComponentStorage<T, M> componentStorage);

    /**
     * Get the storage within the given capability provider.
     * @param capabilityGetter A capability provider.
     * @param context The context to get the storage with.
     * @return A storage, or null if it does not exist.
     */
    public Optional<S> getStorage(ICapabilityGetter<C> capabilityGetter, C context);

    /**
     * Get the ingredient storage within the given capability provider.
     * @param capabilityGetter A capability provider.
     * @param context The context to get the storage with.
     * @return An ingredient storage, or null if it does not exist.
     */
    public default IIngredientComponentStorage<T, M> getComponentStorage(ICapabilityGetter<C> capabilityGetter,
                                                                         C context) {
        Optional<S> storage = getStorage(capabilityGetter, context);
        return storage.map(this::wrapComponentStorage).orElseGet(() -> new IngredientComponentStorageEmpty<>(getComponent()));
    }

}
