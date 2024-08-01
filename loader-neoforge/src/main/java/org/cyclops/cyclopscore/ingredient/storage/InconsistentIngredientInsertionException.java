package org.cyclops.cyclopscore.ingredient.storage;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;

/**
 * An exception that can be thrown when ingredients are being moved,
 * and some ingredient could not be inserted into a destination storage,
 * even though the simulation declared that this insertion would be allowed.
 *
 * This is typically thrown when the remainder ingredient has nowhere else
 * to go, and should be handled exceptionally to avoid it going lost.
 */
public class InconsistentIngredientInsertionException extends Exception {

    private final IngredientComponent<?, ?> ingredientComponent;
    private final IIngredientComponentStorage<?, ?> destination;
    private final Object remainder;
    private final Object movedActual;

    public InconsistentIngredientInsertionException(IngredientComponent<?, ?> ingredientComponent,
                                                    IIngredientComponentStorage<?, ?> destination,
                                                    Object remainder, Object movedActual) {
        this.ingredientComponent = ingredientComponent;
        this.destination = destination;
        this.remainder = remainder;
        this.movedActual = movedActual;
    }

    public <T, M> IngredientComponent<T, M> getIngredientComponent() {
        return (IngredientComponent<T, M>) ingredientComponent;
    }

    public <T, M> IIngredientComponentStorage<T, M> getDestination() {
        return (IIngredientComponentStorage<T, M>) destination;
    }

    /**
     * @param <T> The instance type.
     * @return The instance that could not be re-inserted and would be lost.
     */
    public <T> T getRemainder() {
        return (T) remainder;
    }

    /**
     * @param <T> The instance type.
     * @return The instance that was successfully moved.
     */
    public <T> T getMovedActual() {
        return (T) movedActual;
    }

    @Override
    public String toString() {
        return "Source to destination movement failed " +
                "due to inconsistent insertion behaviour by destination in simulation " +
                "and non-simulation: " + destination + ". Lost: " + remainder +
                ". This can be caused by invalid network setups.";
    }
}
