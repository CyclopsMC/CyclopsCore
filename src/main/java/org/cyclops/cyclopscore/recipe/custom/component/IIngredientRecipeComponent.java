package org.cyclops.cyclopscore.recipe.custom.component;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
 * Interface for recipe components that hold an {@link Ingredient}.
 * @author immortaleeb
 */
public interface IIngredientRecipeComponent {
    /**
     * @return Returns the Ingredient held by this recipe component.
     */
    public Ingredient getIngredient();

    /**
     * @return Returns the ItemStack held by this recipe component.
     */
    public ItemStack getFirstItemStack();
}
