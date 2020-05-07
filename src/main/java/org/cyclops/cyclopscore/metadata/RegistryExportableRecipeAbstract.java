package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import org.cyclops.cyclopscore.helper.CraftingHelpers;

import java.util.function.Supplier;

/**
 * An abstract recipe exporter for {@link IRecipe} recipes.
 */
public abstract class RegistryExportableRecipeAbstract<T extends IRecipeType<? extends R>, R extends IRecipe<C>, C extends IInventory> implements IRegistryExportable {

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
