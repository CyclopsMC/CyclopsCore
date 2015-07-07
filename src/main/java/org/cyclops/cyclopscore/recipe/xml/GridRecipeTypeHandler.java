package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Common handler for both shaped and shapeless recipes.
 * @author rubensworks
 *
 */
public abstract class GridRecipeTypeHandler extends CommonRecipeTypeHandler {
	
	@Override
	public ItemStack loadRecipe(RecipeHandler recipeHandler, Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		
		ItemStack outputItem = (ItemStack) getItem(recipeHandler, output.getElementsByTagName("item").item(0));
		
		handleIO(recipeHandler, input, outputItem);
        return outputItem;
	}
	
	protected abstract void handleIO(RecipeHandler recipeHandler, Element input, ItemStack output) throws XmlRecipeLoader.XmlRecipeException;
	
}
