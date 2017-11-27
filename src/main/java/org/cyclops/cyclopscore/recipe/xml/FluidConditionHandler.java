package org.cyclops.cyclopscore.recipe.xml;

import net.minecraftforge.fluids.FluidRegistry;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if a fluid has been registered.
 * @author rubensworks
 *
 */
public class FluidConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		return FluidRegistry.isFluidRegistered(param);
	}

}
