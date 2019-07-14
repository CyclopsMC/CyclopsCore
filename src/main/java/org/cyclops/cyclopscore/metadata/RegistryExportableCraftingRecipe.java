package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Crafting recipe exporter.
 */
public class RegistryExportableCraftingRecipe implements IRegistryExportable {

    @Override
    public JsonObject export() {
        JsonObject elements = new JsonObject();
        for (ResourceLocation key : CraftingManager.REGISTRY.getKeys()) {
            IRecipe value = CraftingManager.REGISTRY.getObject(key);
            JsonObject serializedRecipe = serializeRecipe(value);
            ItemStack outputItem = value.getRecipeOutput();
            String outputKey = outputItem.getItem().getRegistryName().toString();
            if (!elements.has(outputKey)) {
                elements.add(outputKey, new JsonArray());
            }
            elements.getAsJsonArray(outputKey).add(serializedRecipe);
        }
        return elements;
    }

    @Override
    public String getName() {
        return "crafting_recipe";
    }

    public JsonObject serializeRecipe(IRecipe recipe) {
        JsonObject object = new JsonObject();

        NonNullList<Ingredient> inputs = recipe.getIngredients();
        JsonArray arrayInputs = new JsonArray();
        for (Ingredient input : inputs) {
            JsonArray arrayInputAlternatives = new JsonArray();
            for (ItemStack inputAlternative : input.getMatchingStacks()) {
                arrayInputAlternatives.add(IRegistryExportable.serializeItemStack(inputAlternative));
            }
            arrayInputs.add(arrayInputAlternatives);
        }
        object.add("input", arrayInputs);
        object.add("output", IRegistryExportable.serializeItemStack(recipe.getRecipeOutput()));

        if(recipe instanceof IShapedRecipe) {
            object.addProperty("width", ((IShapedRecipe) recipe).getRecipeWidth());
            object.addProperty("height", ((IShapedRecipe) recipe).getRecipeHeight());
        }

        return object;
    }

}
