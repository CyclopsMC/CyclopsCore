package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if a fluid has been registered.
 * @author rubensworks
 *
 */
public class FluidConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		return ForgeRegistries.FLUIDS.containsKey(new ResourceLocation(param));
	}

}
