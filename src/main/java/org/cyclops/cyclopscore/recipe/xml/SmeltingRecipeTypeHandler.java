package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.Recipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Recipe type handler for smelting recipes.
 * @author rubensworks
 */
public class SmeltingRecipeTypeHandler extends CommonRecipeTypeHandler<IngredientRecipeComponent, IngredientRecipeComponent, DummyPropertiesComponent> {

	@Override
	public String getCategoryId() {
		return "furnace_recipe";
	}

	@Override
	public IRecipe<IngredientRecipeComponent, IngredientRecipeComponent, DummyPropertiesComponent> loadRecipe(RecipeHandler recipeHandler, Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		
		ItemStack inputItem = getSafeItem(getIngredient(recipeHandler, input.getElementsByTagName("item").item(0)));
		ItemStack outputItem = getSafeItem(getIngredient(recipeHandler, output.getElementsByTagName("item").item(0)));
		float xp = 0;
		if(output.getElementsByTagName("xp").getLength() > 0) {
			xp = Float.parseFloat(output.getElementsByTagName("xp").item(0).getTextContent());
		}
		
		GameRegistry.addSmelting(inputItem, outputItem, xp);
        return new Recipe<>(
				new IngredientRecipeComponent(inputItem),
				new IngredientRecipeComponent(outputItem),
				new DummyPropertiesComponent()
		);
	}

	private ItemStack getSafeItem(Ingredient ingredient) {
		return ingredient.getMatchingStacks().length > 0 ? ingredient.getMatchingStacks()[0] : ItemStack.EMPTY;
	}

}
