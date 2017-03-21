package org.cyclops.cyclopscore.recipe.xml;

import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;
import org.w3c.dom.Node;

/**
 * Handler for a specific type of recipe.
 * @author rubensworks
 */
public interface IRecipeTypeHandler<I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> {

	/**
	 * @return The category id of this handler.
	 */
	public String getCategoryId();

	/**
	 * Load the given recipe stored in an xml node for this recipe type.
	 * @param recipeHandler The handler.
	 * @param recipe The recipe node.
     * @return output
	 */
	public IRecipe<I, O, P> loadRecipe(RecipeHandler recipeHandler, Node recipe);
	
}
