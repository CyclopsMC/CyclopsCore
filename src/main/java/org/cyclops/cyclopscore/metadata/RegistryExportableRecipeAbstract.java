package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.Registry;
import org.cyclops.cyclopscore.helper.CraftingHelpers;

import java.util.function.Supplier;

/**
 * An abstract recipe exporter for {@link IRecipe} recipes.
 */
public abstract class RegistryExportableRecipeAbstract<T extends RecipeType<? extends R>, R extends Recipe<C>, C extends Container> implements IRegistryExportable {

    private final Supplier<T> recipeType;

    protected RegistryExportableRecipeAbstract(Supplier<T> recipeType) {
        this.recipeType = recipeType;
    }

    public T getRecipeType() {
        return recipeType.get();
    }

    @Override
    public JsonObject export() {
        JsonObject element = new JsonObject();
        JsonArray elements = new JsonArray();
        element.add("recipes", elements);

        for (R recipe : CraftingHelpers.findServerRecipes(getRecipeType())) {
            JsonObject serializedRecipe = serializeRecipe(recipe);

            serializedRecipe.addProperty("id", recipe.getId().toString());

            elements.add(serializedRecipe);
        }

        return element;
    }

    @Override
    public String getName() {
        return Registry.RECIPE_TYPE.getKey(getRecipeType()).toString().replaceAll(":", "__");
    }

    public abstract JsonObject serializeRecipe(R recipe);

}
