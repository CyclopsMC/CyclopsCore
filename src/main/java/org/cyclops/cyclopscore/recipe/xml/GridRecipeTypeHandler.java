package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.Recipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStacksRecipeComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Common handler for both shaped and shapeless recipes.
 * @author rubensworks
 *
 */
public abstract class GridRecipeTypeHandler extends CommonRecipeTypeHandler<ItemStacksRecipeComponent, ItemStackRecipeComponent, DummyPropertiesComponent> {

	@Override
	public String getCategoryId() {
		return "craftingRecipe";
	}

	@Override
	public IRecipe<ItemStacksRecipeComponent, ItemStackRecipeComponent, DummyPropertiesComponent> loadRecipe(RecipeHandler recipeHandler, Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		
		ItemStack outputItem = (ItemStack) getItem(recipeHandler, output.getElementsByTagName("item").item(0));

		List<Object> itemStacks = handleIO(recipeHandler, input, outputItem);
        return new Recipe<>(
				new ItemStacksRecipeComponent(itemStacks),
				new ItemStackRecipeComponent(outputItem)
		);
	}
	
	protected abstract List<Object> handleIO(RecipeHandler recipeHandler, Element input, ItemStack output) throws XmlRecipeLoader.XmlRecipeException;
	
}
