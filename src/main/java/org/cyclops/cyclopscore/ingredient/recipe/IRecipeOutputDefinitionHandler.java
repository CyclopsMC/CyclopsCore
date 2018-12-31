package org.cyclops.cyclopscore.ingredient.recipe;

import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;

public interface IRecipeOutputDefinitionHandler<T extends IRecipeOutput> {

    IMixedIngredients toRecipeDefinitionOutput(T recipeOutput);

}
