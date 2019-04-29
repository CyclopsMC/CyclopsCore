package org.cyclops.cyclopscore.ingredient.recipe;

import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;

import java.util.List;
import java.util.Map;

public interface IRecipeInputDefinitionHandler<T extends IRecipeInput> {

    public Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> toRecipeDefinitionInput(T recipeInput);

}
