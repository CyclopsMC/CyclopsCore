package org.cyclops.cyclopscore.recipe.xml;

import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if an oredict key is present.
 * @author rubensworks
 *
 */
public class OreDictConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		return OreDictionary.doesOreNameExist(param);
	}

}
