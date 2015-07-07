package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Recipe type handler for smelting recipes.
 * @author rubensworks
 */
public class SmeltingRecipeTypeHandler extends CommonRecipeTypeHandler {

	@Override
	public ItemStack loadRecipe(RecipeHandler recipeHandler, Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		
		ItemStack inputItem = (ItemStack) getItem(recipeHandler, input.getElementsByTagName("item").item(0));
		ItemStack outputItem = (ItemStack) getItem(recipeHandler, output.getElementsByTagName("item").item(0));
		int xp = 0;
		if(output.getElementsByTagName("xp").getLength() > 0) {
			xp = Integer.parseInt(output.getElementsByTagName("xp").item(0).getTextContent());
		}
		
		GameRegistry.addSmelting(inputItem, outputItem, xp);
        return outputItem;
	}

}
