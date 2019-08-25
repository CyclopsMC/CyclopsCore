package org.cyclops.cyclopscore.recipe.event;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

/**
 * A shaped recipe that is observable in terms of the recipe output.
 * The observer can update the output if it desires to do so.
 * @author rubensworks
 *
 */
public class ObservableShapedRecipe extends ShapedRecipe {
	
	private IRecipeOutputObserver observer;

	/**
	 * Make a new instance.
	 * @param id The recipe id.
	 * @param group The recipe name.
	 * @param recipeWidth The recipe width.
	 * @param recipeHeight The recipe height.
	 * @param recipeItems The recipe items.
	 * @param recipeOutput The recipe output.
	 * @param observer The observer for the output.
	 */
	public ObservableShapedRecipe(ResourceLocation id, String group, int recipeWidth, int recipeHeight,
								  NonNullList<Ingredient> recipeItems, ItemStack recipeOutput, IRecipeOutputObserver observer) {
		super(id, group, recipeWidth, recipeHeight, recipeItems, recipeOutput);
		this.observer = observer;
	}
	
	@Override
	public ItemStack getCraftingResult(CraftingInventory craftingGrid) {
		return observer.getRecipeOutput(craftingGrid, super.getCraftingResult(craftingGrid));
    }

}
