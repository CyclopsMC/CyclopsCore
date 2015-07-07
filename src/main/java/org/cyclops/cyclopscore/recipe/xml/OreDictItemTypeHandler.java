package org.cyclops.cyclopscore.recipe.xml;

import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Item type handler for ore dictionary keys.
 * @author rubensworks
 */
public class OreDictItemTypeHandler extends DefaultItemTypeHandler {

	@Override
	protected Object makeItemStack(RecipeHandler recipeHandler, String key, int amount, int meta) {
        return key;
    }
	
}
