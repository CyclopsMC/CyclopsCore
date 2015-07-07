package org.cyclops.cyclopscore.recipe.xml;

import net.minecraftforge.fml.common.Loader;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if mods are available.
 * @author rubensworks
 *
 */
public class ModRecipeConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		return Loader.isModLoaded(param);
	}

}
