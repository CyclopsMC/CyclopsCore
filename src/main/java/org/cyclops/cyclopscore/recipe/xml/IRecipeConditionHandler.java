package org.cyclops.cyclopscore.recipe.xml;

import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Handler to check a type of recipe condition.
 * @author rubensworks
 *
 */
public interface IRecipeConditionHandler {

	/**
	 * Check if this condition is satisfied for the given parameter.
	 * @param recipeHandler The handler.
	 * @param param The condition parameter.
	 * @return If this condition is satisfied.
	 */
	public boolean isSatisfied(RecipeHandler recipeHandler, String param);
	
}
