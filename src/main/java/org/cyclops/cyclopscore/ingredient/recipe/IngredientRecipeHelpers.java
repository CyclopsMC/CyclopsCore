package org.cyclops.cyclopscore.ingredient.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Helpers for the conversion between {@link org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition}
 * and {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe}.
 */
public class IngredientRecipeHelpers {

    public static <T extends IRecipeInput> Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> toRecipeDefinitionInput(T recipeInput) {
        IRecipeInputDefinitionHandler<T> handler = RecipeInputOutputDefinitionHandlers.REGISTRY.getRecipeInputHandler((Class<T>) recipeInput.getClass());
        Objects.requireNonNull(handler, "No handler was registered for " + recipeInput.getClass());
        return handler.toRecipeDefinitionInput(recipeInput);
    }

    public static <T extends IRecipeOutput> IMixedIngredients toRecipeDefinitionOutput(T recipeOutput) {
        IRecipeOutputDefinitionHandler<T> handler = RecipeInputOutputDefinitionHandlers.REGISTRY.getRecipeOutputHandler((Class<T>) recipeOutput.getClass());
        Objects.requireNonNull(handler, "No handler was registered for " + recipeOutput.getClass());
        return handler.toRecipeDefinitionOutput(recipeOutput);
    }

    public static Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> mergeInputs(IRecipeInput... recipeInputs) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        for (IRecipeInput recipeInput : recipeInputs) {
            Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> subInput = toRecipeDefinitionInput(recipeInput);
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

    public static IMixedIngredients mergeOutputs(IRecipeOutput... recipeOutputs) {
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();

        for (IRecipeOutput recipeOutput : recipeOutputs) {
            IMixedIngredients subOutput = toRecipeDefinitionOutput(recipeOutput);
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
