package org.cyclops.cyclopscore.recipe.xml;

import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Recipe type handler for super recipes.
 * @author rubensworks
 */
public abstract class SuperRecipeTypeHandler<I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> extends CommonRecipeTypeHandler<I, O, P> {

	@Override
	public IRecipe<I, O, P> loadRecipe(RecipeHandler recipeHandler, Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		Element properties = (Element) recipeElement.getElementsByTagName("properties").item(0);
		return handleRecipe(recipeHandler, input, output, properties);
	}
	
	protected abstract IRecipe<I, O, P> handleRecipe(RecipeHandler recipeHandler, Element input, Element output, Element properties)
			throws XmlRecipeLoader.XmlRecipeException;

}
