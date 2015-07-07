package org.cyclops.cyclopscore.recipe.xml;


import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if configs are enabled.
 * @author rubensworks
 *
 */
public class PredefinedRecipeConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		return recipeHandler.isPredefinedValue(param);
	}

}
