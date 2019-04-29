package org.cyclops.cyclopscore.ingredient.recipe.handler;

import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeInputDefinitionHandler;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeOutputDefinitionHandler;
import org.cyclops.cyclopscore.ingredient.recipe.IngredientRecipeHelpers;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientsAndFluidStackRecipeComponent;

import java.util.List;
import java.util.Map;

public class IngredientsAndFluidStackRecipeComponentHandler implements IRecipeInputDefinitionHandler<IngredientsAndFluidStackRecipeComponent>,
        IRecipeOutputDefinitionHandler<IngredientsAndFluidStackRecipeComponent> {
    @Override
    public IMixedIngredients toRecipeDefinitionOutput(IngredientsAndFluidStackRecipeComponent recipeOutput) {
        return IngredientRecipeHelpers.mergeOutputs(recipeOutput.getIngredientsComponent(), recipeOutput.getFluidStackComponent());
    }

    @Override
    public Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> toRecipeDefinitionInput(IngredientsAndFluidStackRecipeComponent recipeInput) {
        return IngredientRecipeHelpers.mergeInputs(recipeInput.getIngredientsComponent(), recipeInput.getFluidStackComponent());
    }
}
