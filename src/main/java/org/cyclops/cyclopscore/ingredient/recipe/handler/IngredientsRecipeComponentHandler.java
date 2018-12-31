package org.cyclops.cyclopscore.ingredient.recipe.handler;

import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeInputDefinitionHandler;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeOutputDefinitionHandler;
import org.cyclops.cyclopscore.ingredient.recipe.IngredientRecipeHelpers;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientsRecipeComponent;

import java.util.List;
import java.util.Map;

public class IngredientsRecipeComponentHandler implements IRecipeInputDefinitionHandler<IngredientsRecipeComponent>,
        IRecipeOutputDefinitionHandler<IngredientsRecipeComponent> {
    @Override
    public IMixedIngredients toRecipeDefinitionOutput(IngredientsRecipeComponent recipeOutput) {
        return IngredientRecipeHelpers.mergeOutputs(recipeOutput.getIngredientComponents().toArray(new IngredientRecipeComponent[0]));
    }

    @Override
    public Map<IngredientComponent<?, ?>, List<List<IPrototypedIngredient<?, ?>>>> toRecipeDefinitionInput(IngredientsRecipeComponent recipeInput) {
        return IngredientRecipeHelpers.mergeInputs(recipeInput.getIngredientComponents().toArray(new IngredientRecipeComponent[0]));
    }
}
