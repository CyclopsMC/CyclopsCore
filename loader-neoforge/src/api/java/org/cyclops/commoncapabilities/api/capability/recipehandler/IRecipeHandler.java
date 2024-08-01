package org.cyclops.commoncapabilities.api.capability.recipehandler;

import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

/**
 * An IRecipeHandler is able to process recipes.
 *
 * It is used to expose the possible recipes the target can handle,
 * and simulate what output will be produced based on a certain input.
 *
 * It may also expose further information on how the inputs and outputs
 * must be handled.
 *
 * @author rubensworks
 */
public interface IRecipeHandler {

    /**
     * @return The input recipe component types that are possible for recipes in this handler.
     */
    public Set<IngredientComponent<?, ?>> getRecipeInputComponents();

    /**
     * @return The output recipe component types that are possible for recipes in this handler.
     */
    public Set<IngredientComponent<?, ?>> getRecipeOutputComponents();

    /**
     * Check if the given size of recipe component instances are valid for the given recipe component type.
     * @param component The component type.
     * @param size      A certain length of recipe component instances.
     * @return If the given size of recipe component instances can be used by this recipe handler.
     */
    public boolean isValidSizeInput(IngredientComponent<?, ?> component, int size);

    /**
     * @return Recipes that are available through this handler, this list is not necessarily exhaustive, but SHOULD be.
     *         No consistent order should be assumed from in collection.
     */
    public Collection<IRecipeDefinition> getRecipes();

    /**
     * Test if the given recipe input can be handled by this handler.
     *
     * This method is mainly meant to be indicative.
     * No actual crafting processes should be crafted because of this,
     * i.e., calling this method should not have any effects on the state of this recipe handler.
     *
     * @param input A recipe input.
     * @return The simulated output, or null if no valid recipe for the given input was found.
     */
    @Nullable
    public IMixedIngredients simulate(IMixedIngredients input);

}
