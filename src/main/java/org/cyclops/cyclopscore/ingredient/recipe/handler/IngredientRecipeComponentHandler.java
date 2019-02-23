package org.cyclops.cyclopscore.ingredient.recipe.handler;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
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
    public Map<IngredientComponent<?, ?>, List<List<IPrototypedIngredient<?, ?>>>> toRecipeDefinitionInput(IngredientRecipeComponent recipeInput) {
        Map<IngredientComponent<?, ?>, List<List<IPrototypedIngredient<?, ?>>>> inputs = Maps.newIdentityHashMap();
        List<ItemStack> itemStacks = recipeInput.getItemStacks();
        if (!itemStacks.isEmpty()) {
            inputs.put(IngredientComponent.ITEMSTACK, Collections.singletonList(itemStacks
                    .stream()
                    .map(itemStack -> {
                        int condition = ItemMatch.ITEM;
                        if (itemStack.getItemDamage() != IngredientRecipeComponent.META_WILDCARD) {
                            condition |= ItemMatch.DAMAGE;
                        }
                        return new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, itemStack, condition);
                    })
                    .collect(Collectors.toList())));
        }
        return inputs;
    }
}
