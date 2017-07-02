package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Item type handler for ore dictionary keys.
 * @author rubensworks
 */
public class OreDictItemTypeHandler extends DefaultItemTypeHandler {

	@Override
	protected Ingredient makeIngredient(RecipeHandler recipeHandler, String key, int amount, int meta, boolean nbtSensitive) {
        if (nbtSensitive) {
            throw new IllegalArgumentException("NBT-sensitivity can not be defined on oredict ingredients: " + key);
        }
        return new OreIngredient(key);
    }
	
}
