package org.cyclops.cyclopscore.metadata;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry;

import java.util.Map;
import java.util.function.Supplier;

/**
 * An abstract recipe exporter for {@link IRecipeRegistry} recipes.
 */
public abstract class RegistryExportableRecipeAbstract<I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> implements IRegistryExportable {

    private final Supplier<IRecipeRegistry<?, I, O, P>> recipeRegistry;
    private final String name;

    protected RegistryExportableRecipeAbstract(Supplier<IRecipeRegistry<?, I, O, P>> recipeRegistry, String name) {
        this.recipeRegistry = recipeRegistry;
        this.name = name;
    }

    public IRecipeRegistry<?, I, O, P> getRecipeRegistry() {
        return recipeRegistry.get();
    }

    @Override
    public JsonObject export() {
        JsonObject element = new JsonObject();
        JsonArray elements = new JsonArray();
        element.add("recipes", elements);

        // Calculate tags for all recipes
        Multimap<IRecipe, String> taggedRecipeReverse = Multimaps.newListMultimap(Maps.newHashMap(), Lists::newArrayList);
        for (Map.Entry<String, IRecipe> entry : getRecipeRegistry().getMod().getRecipeHandler().getTaggedRecipes().entries()) {
            taggedRecipeReverse.put(entry.getValue(), entry.getKey());
        }

        for (IRecipe<I, O, P> recipe : getRecipeRegistry().allRecipes()) {
            JsonObject serializedRecipe = serializeRecipe(recipe);

            JsonArray tagsArray = new JsonArray();
            for (String tag : taggedRecipeReverse.get(recipe)) {
                tagsArray.add(tag);
            }
            serializedRecipe.add("tags", tagsArray);

            elements.add(serializedRecipe);
        }

        return element;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public abstract JsonObject serializeRecipe(IRecipe<I, O, P> recipe);

}
