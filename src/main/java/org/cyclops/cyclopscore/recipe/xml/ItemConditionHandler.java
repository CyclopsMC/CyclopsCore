package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if an item has been registered.
 * @author rubensworks
 *
 */
public class ItemConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		return Item.REGISTRY.containsKey(new ResourceLocation(param));
	}

}
