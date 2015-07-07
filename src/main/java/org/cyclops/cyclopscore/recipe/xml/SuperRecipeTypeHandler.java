package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Recipe type handler for super recipes.
 * @author rubensworks
 */
public abstract class SuperRecipeTypeHandler extends CommonRecipeTypeHandler {

	@Override
	public ItemStack loadRecipe(RecipeHandler recipeHandler, Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		Element properties = (Element) recipeElement.getElementsByTagName("properties").item(0);
		return handleRecipe(recipeHandler, input, output, properties);
	}
	
	protected abstract ItemStack handleRecipe(RecipeHandler recipeHandler, Element input, Element output, Element properties)
			throws XmlRecipeLoader.XmlRecipeException;

}
