package org.cyclops.cyclopscore.ingredient.recipe.handler;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeInputDefinitionHandler;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeOutputDefinitionHandler;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IngredientRecipeComponentHandler implements IRecipeInputDefinitionHandler<IngredientRecipeComponent>,
        IRecipeOutputDefinitionHandler<IngredientRecipeComponent> {
    @Override
    public IMixedIngredients toRecipeDefinitionOutput(IngredientRecipeComponent recipeOutput) {
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();
        if (!recipeOutput.getItemStacks().isEmpty() && recipeOutput.getChance() == 1.0F) {
            outputs.put(IngredientComponent.ITEMSTACK, Collections.singletonList(recipeOutput.getFirstItemStack()));
        }
        return new MixedIngredients(outputs);
    }

    @Override
    public Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> toRecipeDefinitionInput(IngredientRecipeComponent recipeInput) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        List<ItemStack> itemStacks = recipeInput.getItemStacks();
        if (!itemStacks.isEmpty()) {
            inputs.put(IngredientComponent.ITEMSTACK, Collections.singletonList(new PrototypedIngredientAlternativesList<>(
                    itemStacks
                            .stream()
                            .map(itemStack -> new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, itemStack, ItemMatch.ITEM))
                            .collect(Collectors.toList()))));
        }
        return inputs;
    }
}
