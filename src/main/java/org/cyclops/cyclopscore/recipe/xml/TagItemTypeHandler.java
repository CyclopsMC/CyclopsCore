package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Item type handler for tags.
 * @author rubensworks
 */
public class TagItemTypeHandler extends DefaultItemTypeHandler {

	@Override
	protected Ingredient makeIngredient(RecipeHandler recipeHandler, String key, int amount, boolean nbtSensitive) {
        if (nbtSensitive) {
            throw new IllegalArgumentException("NBT-sensitivity can not be defined on oredict ingredients: " + key);
        }
        return Ingredient.fromTag(ItemTags.getCollection().get(new ResourceLocation(key)));
    }
	
}
