package org.cyclops.cyclopscore.ingredient.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Helpers for the conversion between {@link org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition}
 * and {@link IRecipe}.
 */
public class IngredientRecipeHelpers {

    public static <T extends IRecipeType<R>, C extends IInventory, R extends IRecipe<C>>
    Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> toRecipeDefinitionInput(T recipeType, R recipe) {
        IRecipeDefinitionHandler<T, C, R> handler = RecipeDefinitionHandlers.REGISTRY.getRecipeHandler(recipeType);
        Objects.requireNonNull(handler, "No handler was registered for " + recipeType.getClass());
        return handler.toRecipeDefinitionInput(recipe);
    }

    public static <T extends IRecipeType<R>, C extends IInventory, R extends IRecipe<C>>
    IMixedIngredients toRecipeDefinitionOutput(T recipeType, R recipe) {
        IRecipeDefinitionHandler<T, C, R> handler = RecipeDefinitionHandlers.REGISTRY.getRecipeHandler(recipeType);
        Objects.requireNonNull(handler, "No handler was registered for " + recipeType.getClass());
        return handler.toRecipeDefinitionOutput(recipe);
    }

    public static <T extends IRecipeType<R>, C extends IInventory, R extends IRecipe<C>>
    Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> mergeInputs(T recipeType, R... recipes) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        for (R recipe : recipes) {
            Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> subInput = toRecipeDefinitionInput(recipeType, recipe);
            for (Map.Entry<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> entry : subInput.entrySet()) {
                List<IPrototypedIngredientAlternatives<?, ?>> list = inputs.get(entry.getKey());
                if (list == null) {
                    list = Lists.newArrayList();
                    inputs.put(entry.getKey(), list);
                }
                list.addAll(entry.getValue());
            }
        }
        return inputs;
    }

    public static <T extends IRecipeType<R>, C extends IInventory, R extends IRecipe<C>>
    IMixedIngredients mergeOutputs(T recipeType, R... recipes) {
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        for (R recipe : recipes) {
            IMixedIngredients subOutput = toRecipeDefinitionOutput(recipeType, recipe);
            for (IngredientComponent<?, ?> component : subOutput.getComponents()) {
                List<?> list = outputs.get(component);
                if (list == null) {
                    list = Lists.newArrayList();
                    outputs.put(component, list);
                }
                list.addAll((List) subOutput.getInstances(component));
            }
        }

        return new MixedIngredients(outputs);
    }

}
