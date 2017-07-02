package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.Recipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientsRecipeComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Common handler for both shaped and shapeless recipes.
 * @author rubensworks
 *
 */
public abstract class GridRecipeTypeHandler extends CommonRecipeTypeHandler<IngredientsRecipeComponent, IngredientRecipeComponent, DummyPropertiesComponent> {

	@Override
	public String getCategoryId() {
		return "craftingRecipe";
	}

	@Override
	public IRecipe<IngredientsRecipeComponent, IngredientRecipeComponent, DummyPropertiesComponent> loadRecipe(RecipeHandler recipeHandler, Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		
		ItemStack outputItemStack = getIngredient(recipeHandler, output.getElementsByTagName("item").item(0)).getMatchingStacks()[0];

		NonNullList<Ingredient> ingredients = handleIO(recipeHandler, input, outputItemStack);
        return new Recipe<>(
				new IngredientsRecipeComponent(ingredients),
				new IngredientRecipeComponent(outputItemStack)
		);
	}
	
	protected abstract NonNullList<Ingredient> handleIO(RecipeHandler recipeHandler, Element input, ItemStack output)
			throws XmlRecipeLoader.XmlRecipeException;
	
}
