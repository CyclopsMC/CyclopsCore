package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.neoforged.neoforge.common.crafting.IShapedRecipe;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * Crafting recipe exporter.
 */
public class RegistryExportableCraftingRecipe extends RegistryExportableRecipeAbstract<RecipeType<CraftingRecipe>, CraftingRecipe, CraftingContainer> {

    protected RegistryExportableCraftingRecipe() {
        super(() -> RecipeType.CRAFTING);
    }

    @Override
    public JsonObject serializeRecipe(RecipeHolder<CraftingRecipe> recipe) {
        JsonObject object = new JsonObject();

        NonNullList<Ingredient> inputs = recipe.value().getIngredients();
        JsonArray arrayInputs = new JsonArray();
        for (Ingredient input : inputs) {
            JsonArray arrayInputAlternatives = new JsonArray();
            for (ItemStack inputAlternative : input.getItems()) {
                arrayInputAlternatives.add(IRegistryExportable.serializeItemStack(inputAlternative));
            }
            arrayInputs.add(arrayInputAlternatives);
        }
        object.addProperty("id", recipe.id().toString());
        object.add("input", arrayInputs);
        object.add("output", IRegistryExportable.serializeItemStack(recipe.value().getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess())));

        if(recipe.value() instanceof IShapedRecipe) {
            object.addProperty("width", ((IShapedRecipe) recipe.value()).getRecipeWidth());
            object.addProperty("height", ((IShapedRecipe) recipe.value()).getRecipeHeight());
        }

        return object;
    }

}
