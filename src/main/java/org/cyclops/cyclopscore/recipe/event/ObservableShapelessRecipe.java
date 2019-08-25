package org.cyclops.cyclopscore.recipe.event;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

/**
 * A shapeless recipe that is observable in terms of the recipe output.
 * The observer can update the output if it desires to do so.
 * @author rubensworks
 *
 */
public class ObservableShapelessRecipe extends ShapelessRecipe {
	
	private IRecipeOutputObserver observer;
	
	/**
	 * Make a new instance.
	 * @param id The recipe id.
	 * @param group The recipe name.
	 * @param result The result.
	 * @param recipeItems The recipe items.
	 * @param observer The observer for the output.
	 */
	public ObservableShapelessRecipe(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> recipeItems, IRecipeOutputObserver observer) {
		super(id, group, result, recipeItems);
		this.observer = observer;
	}
	
	@Override
	public ItemStack getCraftingResult(CraftingInventory craftingGrid) {
		return observer.getRecipeOutput(craftingGrid, super.getCraftingResult(craftingGrid));
    }

}
