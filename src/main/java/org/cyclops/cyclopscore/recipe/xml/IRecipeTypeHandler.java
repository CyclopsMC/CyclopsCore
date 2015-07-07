package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.w3c.dom.Node;

/**
 * Handler for a specific type of recipe.
 * @author rubensworks
 */
public interface IRecipeTypeHandler {

	/**
	 * Load the given recipe stored in an xml node for this recipe type.
	 * @param recipeHandler The handler.
	 * @param recipe The recipe node.
     * @return output
	 */
	public ItemStack loadRecipe(RecipeHandler recipeHandler, Node recipe);
	
}
