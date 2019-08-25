package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if an item has been registered.
 * @author rubensworks
 *
 */
public class ItemConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		return ForgeRegistries.ITEMS.containsKey(new ResourceLocation(param));
	}

}
