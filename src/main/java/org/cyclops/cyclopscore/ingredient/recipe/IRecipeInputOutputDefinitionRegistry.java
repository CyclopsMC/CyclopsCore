package org.cyclops.cyclopscore.ingredient.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import org.cyclops.cyclopscore.init.IRegistry;

import javax.annotation.Nullable;

/**
 * Regsitry for handlers for for the conversion between
 * {@link org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition} and {@link IRecipe}.
 */
public interface IRecipeInputOutputDefinitionRegistry extends IRegistry {

    public <T extends IRecipeType<R>, C extends IInventory, R extends IRecipe<C>, H extends IRecipeDefinitionHandler<T, C, R>> void setRecipeHandler(T recipeType, H handler);

    @Nullable
    public <T extends IRecipeType<R>, C extends IInventory, R extends IRecipe<C>> IRecipeDefinitionHandler<T, C, R> getRecipeHandler(T recipeType);

}
