package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Item type handler for ore dictionary keys.
 * @author rubensworks
 */
public class PredefinedItemTypeHandler extends DefaultItemTypeHandler {

	@Override
	protected Ingredient makeIngredient(RecipeHandler recipeHandler, String key, int amount, boolean nbtSensitive) throws XmlRecipeLoader.XmlRecipeException {
        ItemStack item = recipeHandler.getPredefinedItem(key);
        if(item == null) {
        	throw new XmlRecipeLoader.XmlRecipeException(String.format(
        			"Could not find the predefined item for key '%s'.", key));
        }
        return createItemIngredient(item, nbtSensitive);
    }
	
}
