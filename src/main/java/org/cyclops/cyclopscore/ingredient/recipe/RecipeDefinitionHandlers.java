package org.cyclops.cyclopscore.ingredient.recipe;

import org.cyclops.cyclopscore.CyclopsCore;

public class RecipeDefinitionHandlers {

    public static IRecipeInputOutputDefinitionRegistry REGISTRY = CyclopsCore._instance.getRegistryManager()
            .getRegistry(IRecipeInputOutputDefinitionRegistry.class);

    public static void load() {

    }

}
