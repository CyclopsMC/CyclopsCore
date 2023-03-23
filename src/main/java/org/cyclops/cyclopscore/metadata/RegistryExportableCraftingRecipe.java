package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Crafting recipe exporter.
 */
public class RegistryExportableCraftingRecipe extends RegistryExportableRecipeAbstract<RecipeType<CraftingRecipe>, CraftingRecipe, CraftingContainer> {

    protected RegistryExportableCraftingRecipe() {
        super(() -> RecipeType.CRAFTING);
    }

    @Override
    public JsonObject serializeRecipe(CraftingRecipe recipe) {
        JsonObject object = new JsonObject();

        NonNullList<Ingredient> inputs = recipe.getIngredients();
        JsonArray arrayInputs = new JsonArray();
        for (Ingredient input : inputs) {
            JsonArray arrayInputAlternatives = new JsonArray();
            for (ItemStack inputAlternative : input.getItems()) {
                arrayInputAlternatives.add(IRegistryExportable.serializeItemStack(inputAlternative));
            }
            arrayInputs.add(arrayInputAlternatives);
        }
        object.addProperty("id", recipe.getId().toString());
        object.add("input", arrayInputs);
        object.add("output", IRegistryExportable.serializeItemStack(recipe.getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess())));

        if(recipe instanceof IShapedRecipe) {
            object.addProperty("width", ((IShapedRecipe) recipe).getRecipeWidth());
            object.addProperty("height", ((IShapedRecipe) recipe).getRecipeHeight());
        }

        return object;
    }

}
