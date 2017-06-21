package org.cyclops.cyclopscore.recipe.event;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * A shapeless recipe that is observable in terms of the recipe output.
 * The observer can update the output if it desires to do so.
 * @author rubensworks
 *
 */
public class ObservableShapelessRecipe extends ShapelessOreRecipe {
	
	private IRecipeOutputObserver observer;
	
	/**
	 * Make a new instance.
	 * @param result The result.
	 * @param recipe The recipe.
	 * @param observer The observer for the output.
	 */
	public ObservableShapelessRecipe(ResourceLocation group, ItemStack result, Object[] recipe, IRecipeOutputObserver observer) {
		super(group, result, recipe);
		this.observer = observer;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting craftingGrid) {
		return observer.getRecipeOutput(craftingGrid, super.getCraftingResult(craftingGrid));
    }

}
