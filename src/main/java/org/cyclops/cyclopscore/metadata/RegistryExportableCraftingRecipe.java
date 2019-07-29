package org.cyclops.cyclopscore.metadata;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;

import java.util.Map;

/**
 * Crafting recipe exporter.
 */
public class RegistryExportableCraftingRecipe implements IRegistryExportable {

    @Override
    public JsonObject export() {
        // Calculate tags for all recipes
        Multimap<ItemStack, String> taggedRecipeReverse = Multimaps.newListMultimap(Maps.newHashMap(), Lists::newArrayList);
        for (ModContainer modContainer : Loader.instance().getActiveModList()) {
            if (modContainer.getMod() instanceof ModBase) {
                RecipeHandler recipeHandler = ((ModBase) modContainer.getMod()).getRecipeHandler();
                if (recipeHandler != null) {
                    for (Map.Entry<String, org.cyclops.cyclopscore.recipe.custom.api.IRecipe> entry : recipeHandler.getTaggedRecipes().entries()) {
                        if (entry.getValue().getOutput() instanceof IngredientRecipeComponent) {
                            ItemStack outputStack = ((IngredientRecipeComponent) entry.getValue().getOutput()).getFirstItemStack();
                            taggedRecipeReverse.put(outputStack, entry.getKey());
                        }
                    }
                }
            }
        }

        JsonObject elements = new JsonObject();
        for (ResourceLocation key : CraftingManager.REGISTRY.getKeys()) {
            IRecipe value = CraftingManager.REGISTRY.getObject(key);
            JsonObject serializedRecipe = serializeRecipe(value);
            ItemStack outputItem = value.getRecipeOutput();

            // Determine tags
            JsonArray tags = new JsonArray();
            for (Map.Entry<ItemStack, String> entry : taggedRecipeReverse.entries()) {
                if (ItemStack.areItemStacksEqual(entry.getKey(), outputItem)) {
                    tags.add(entry.getValue());
                }
            }
            serializedRecipe.add("tags", tags);

            // Add to results
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
