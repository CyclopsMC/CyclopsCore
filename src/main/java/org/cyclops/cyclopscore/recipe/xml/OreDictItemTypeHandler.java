package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import org.cyclops.cyclopscore.init.RecipeHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

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
        return new OreIngredient(key) {
            @Nonnull
            @Override
            public ItemStack[] getMatchingStacks() {
                if (amount > 0) {
                    return Arrays.stream(super.getMatchingStacks()).map(stack -> {
                        stack = stack.copy();
                        stack.setCount(amount);
                        return stack;
                    }).toArray(ItemStack[]::new);
                }
                return super.getMatchingStacks();
            }
        };
    }
	
}
