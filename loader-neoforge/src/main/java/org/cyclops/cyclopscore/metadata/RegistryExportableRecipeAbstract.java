package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import org.cyclops.cyclopscore.helper.CraftingHelpers;

import java.util.function.Supplier;

/**
 * An abstract recipe exporter for {@link Recipe} recipes.
 */
public abstract class RegistryExportableRecipeAbstract<T extends RecipeType<? extends R>, R extends Recipe<C>, C extends RecipeInput> implements IRegistryExportable {

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

        for (RecipeHolder<R> recipeHolder : CraftingHelpers.findServerRecipes(getRecipeType())) {
            JsonObject serializedRecipe = serializeRecipe(recipeHolder);

            serializedRecipe.addProperty("id", recipeHolder.toString());

            elements.add(serializedRecipe);
        }

        return element;
    }

    @Override
    public String getName() {
        return BuiltInRegistries.RECIPE_TYPE.getKey(getRecipeType()).toString().replaceAll(":", "__");
    }

    public abstract JsonObject serializeRecipe(RecipeHolder<R> recipe);

}
