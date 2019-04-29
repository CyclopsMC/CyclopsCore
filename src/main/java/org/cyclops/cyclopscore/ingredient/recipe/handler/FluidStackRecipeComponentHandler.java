package org.cyclops.cyclopscore.ingredient.recipe.handler;

import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.PrototypedIngredientAlternativesList;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeInputDefinitionHandler;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeOutputDefinitionHandler;
import org.cyclops.cyclopscore.recipe.custom.component.FluidStackRecipeComponent;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FluidStackRecipeComponentHandler implements IRecipeInputDefinitionHandler<FluidStackRecipeComponent>,
        IRecipeOutputDefinitionHandler<FluidStackRecipeComponent> {
    @Override
    public IMixedIngredients toRecipeDefinitionOutput(FluidStackRecipeComponent recipeOutput) {
        Map<IngredientComponent<?, ?>, List<?>> outputs = Maps.newIdentityHashMap();
        if (recipeOutput.getFluidStack() != null) {
            outputs.put(IngredientComponent.FLUIDSTACK, Collections.singletonList(recipeOutput.getFluidStack()));
        }
        return new MixedIngredients(outputs);
    }

    @Override
    public Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> toRecipeDefinitionInput(FluidStackRecipeComponent recipeInput) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        if (recipeInput.getFluidStack() != null) {
            inputs.put(IngredientComponent.FLUIDSTACK, Collections.singletonList(
                    new PrototypedIngredientAlternativesList<>(Collections.singletonList(
                            new PrototypedIngredient<>(IngredientComponent.FLUIDSTACK, recipeInput.getFluidStack(), FluidMatch.FLUID)
                    ))));
        }
        return inputs;
    }
}
