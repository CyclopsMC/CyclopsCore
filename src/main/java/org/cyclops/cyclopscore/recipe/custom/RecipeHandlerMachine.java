package org.cyclops.cyclopscore.recipe.custom;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.recipe.IngredientRecipeHelpers;
import org.cyclops.cyclopscore.recipe.custom.api.IMachine;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

/**
 * An adapter class for exposing an {@link IMachine} as an {@link IRecipeHandler}.
 * @param <M> The type of the machine.
 * @param <I> The type of the recipe input of all recipes associated with the machine.
 * @param <O> The type of the recipe output of all recipes associated with the machine.
 * @param <P> The type of the recipe properties of all recipes associated with the machine.
 */
public abstract class RecipeHandlerMachine<M extends IMachine<M, I, O, P>,
        I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> implements IRecipeHandler {

    private final M machine;
    private final Set<IngredientComponent<?, ?>> recipeInputComponents;
    private final Set<IngredientComponent<?, ?>> recipeOutputComponents;

    public RecipeHandlerMachine(M machine,
                                Set<IngredientComponent<?, ?>> recipeInputComponents,
                                Set<IngredientComponent<?, ?>> recipeOutputComponents) {
        this.machine = machine;
        this.recipeInputComponents = recipeInputComponents;
        this.recipeOutputComponents = recipeOutputComponents;
    }

    @Override
    public Set<IngredientComponent<?, ?>> getRecipeInputComponents() {
        return recipeInputComponents;
    }

    @Override
    public Set<IngredientComponent<?, ?>> getRecipeOutputComponents() {
        return recipeOutputComponents;
    }

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        return Lists.transform(machine.getRecipeRegistry().allRecipes(), getRecipeTransformer());
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        IRecipe<I, O, P> recipe = machine.getRecipeRegistry().findRecipeByInput(inputIngredientsToRecipeInput(input));
        if (recipe == null) {
            return null;
        }
        return getRecipeTransformer().apply(recipe).getOutput();
    }

    protected Function<IRecipe<I, O, P>, IRecipeDefinition> getRecipeTransformer() {
        return recipe -> new RecipeDefinition(
                IngredientRecipeHelpers.toRecipeDefinitionInput(recipe.getInput()),
                IngredientRecipeHelpers.toRecipeDefinitionOutput(recipe.getOutput())
        );
    }

    protected abstract I inputIngredientsToRecipeInput(IMixedIngredients inputIngredients);
}
