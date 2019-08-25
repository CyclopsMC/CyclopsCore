package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

/**
 * Furnace recipe exporter.
 */
public class RegistryExportableFurnaceRecipe implements IRegistryExportable {

    @Override
    public JsonObject export() {
        JsonObject root = new JsonObject();
        JsonArray elements = new JsonArray();
        for (IRecipe<IInventory> recipe: ServerLifecycleHooks.getCurrentServer().getRecipeManager().getRecipes(IRecipeType.SMELTING).values()) {
            elements.add(serializeRecipe(recipe));
        }
        root.add("elements", elements);
        return root;
    }

    @Override
    public String getName() {
        return "furnace_recipe";
    }

    public JsonObject serializeRecipe(IRecipe<IInventory> recipe) {
        JsonObject object = new JsonObject();
        JsonArray variants = new JsonArray();
        for (Ingredient ingredient : recipe.getIngredients()) {
            for (ItemStack matchingStack : ingredient.getMatchingStacks()) {
                variants.add(IRegistryExportable.serializeItemStack(matchingStack));
            }
        }
        object.add("input", variants);
        object.add("output", IRegistryExportable.serializeItemStack(recipe.getRecipeOutput()));
        return object;
    }

}
