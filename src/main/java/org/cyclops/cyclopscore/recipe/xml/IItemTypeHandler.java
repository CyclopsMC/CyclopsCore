package org.cyclops.cyclopscore.recipe.xml;

import org.cyclops.cyclopscore.init.RecipeHandler;
import org.w3c.dom.Node;

/**
 * Handler for different types of item declaration inside the xml recipe file.
 * @author rubensworks
 *
 */
public interface IItemTypeHandler {

	/**
	 * Get the item for this type of handler for the given node element, can just be a text node.
	 * @param recipeHandler The handler.
	 * @param itemNode The node containing info about this item, for example
	 * "&lt;item&gt;evilcraft:darkGem&lt;/item&gt;"
	 * @return An item stack for this item or a string representing an ore dict id.
	 * @throws XmlRecipeLoader.XmlRecipeException If an error occured
	 */
	public Object getItem(RecipeHandler recipeHandler, Node itemNode) throws XmlRecipeLoader.XmlRecipeException;
	
}
