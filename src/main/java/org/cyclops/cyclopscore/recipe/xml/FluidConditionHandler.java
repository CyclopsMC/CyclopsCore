package org.cyclops.cyclopscore.recipe.xml;

import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if a fluid has been registered.
 * @author rubensworks
 *
 */
public class FluidConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		return false;
		// return FluidRegistry.isFluidRegistered(param); // // TODO: update when Forge is updated with Fluids.
	}

}
