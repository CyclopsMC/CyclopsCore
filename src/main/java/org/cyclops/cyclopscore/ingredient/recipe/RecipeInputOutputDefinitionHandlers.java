package org.cyclops.cyclopscore.ingredient.recipe;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.ingredient.recipe.handler.FluidStackRecipeComponentHandler;
import org.cyclops.cyclopscore.ingredient.recipe.handler.IngredientAndFluidStackRecipeComponentHandler;
import org.cyclops.cyclopscore.ingredient.recipe.handler.IngredientRecipeComponentHandler;
import org.cyclops.cyclopscore.ingredient.recipe.handler.IngredientsAndFluidStackRecipeComponentHandler;
import org.cyclops.cyclopscore.ingredient.recipe.handler.IngredientsRecipeComponentHandler;
import org.cyclops.cyclopscore.recipe.custom.component.FluidStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientAndFluidStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientsAndFluidStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientsRecipeComponent;

public class RecipeInputOutputDefinitionHandlers {

    public static IRecipeInputOutputDefinitionRegistry REGISTRY = CyclopsCore._instance.getRegistryManager()
            .getRegistry(IRecipeInputOutputDefinitionRegistry.class);

    public static void load() {
        REGISTRY.setRecipeInputOutputHandler(FluidStackRecipeComponent.class, new FluidStackRecipeComponentHandler());
        REGISTRY.setRecipeInputOutputHandler(IngredientAndFluidStackRecipeComponent.class, new IngredientAndFluidStackRecipeComponentHandler());
        REGISTRY.setRecipeInputOutputHandler(IngredientRecipeComponent.class, new IngredientRecipeComponentHandler());
        REGISTRY.setRecipeInputOutputHandler(IngredientsAndFluidStackRecipeComponent.class, new IngredientsAndFluidStackRecipeComponentHandler());
        REGISTRY.setRecipeInputOutputHandler(IngredientsRecipeComponent.class, new IngredientsRecipeComponentHandler());
    }

}
