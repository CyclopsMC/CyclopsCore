package org.cyclops.cyclopscore.recipe.xml;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if configs are enabled.
 * @author rubensworks
 *
 */
public class ConfigRecipeConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		ExtendedConfig<?> config = recipeHandler.getMod().getConfigHandler().getDictionary().get(param);
		return config != null && config.isEnabled();
	}

}
