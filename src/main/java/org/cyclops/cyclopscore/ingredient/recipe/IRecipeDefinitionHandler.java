package org.cyclops.cyclopscore.ingredient.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.List;
import java.util.Map;

public interface IRecipeDefinitionHandler<T extends IRecipeType<R>, C extends IInventory, R extends IRecipe<C>> {

    public Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> toRecipeDefinitionInput(R recipe);

    public IMixedIngredients toRecipeDefinitionOutput(R recipe);

}
