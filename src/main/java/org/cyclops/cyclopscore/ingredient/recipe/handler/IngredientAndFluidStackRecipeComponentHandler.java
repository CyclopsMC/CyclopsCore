package org.cyclops.cyclopscore.ingredient.recipe.handler;

import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeInputDefinitionHandler;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeOutputDefinitionHandler;
import org.cyclops.cyclopscore.ingredient.recipe.IngredientRecipeHelpers;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientAndFluidStackRecipeComponent;

import java.util.List;
import java.util.Map;

public class IngredientAndFluidStackRecipeComponentHandler implements IRecipeInputDefinitionHandler<IngredientAndFluidStackRecipeComponent>,
        IRecipeOutputDefinitionHandler<IngredientAndFluidStackRecipeComponent> {
    @Override
    public IMixedIngredients toRecipeDefinitionOutput(IngredientAndFluidStackRecipeComponent recipeOutput) {
        return IngredientRecipeHelpers.mergeOutputs(recipeOutput.getIngredientComponent(), recipeOutput.getFluidStackComponent());
    }

    @Override
    public Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> toRecipeDefinitionInput(IngredientAndFluidStackRecipeComponent recipeInput) {
        return IngredientRecipeHelpers.mergeInputs(recipeInput.getIngredientComponent(), recipeInput.getFluidStackComponent());
    }
}
