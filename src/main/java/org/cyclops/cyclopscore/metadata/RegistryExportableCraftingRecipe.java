package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Crafting recipe exporter.
 */
public class RegistryExportableCraftingRecipe extends RegistryExportableRecipeAbstract<IRecipeType<ICraftingRecipe>, ICraftingRecipe, CraftingInventory> {

    protected RegistryExportableCraftingRecipe() {
        super(() -> IRecipeType.CRAFTING);
    }

    @Override
    public JsonObject serializeRecipe(ICraftingRecipe recipe) {
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
        object.addProperty("id", recipe.getId().toString());
        object.add("input", arrayInputs);
        object.add("output", IRegistryExportable.serializeItemStack(recipe.getRecipeOutput()));

        if(recipe instanceof IShapedRecipe) {
            object.addProperty("width", ((IShapedRecipe) recipe).getRecipeWidth());
            object.addProperty("height", ((IShapedRecipe) recipe).getRecipeHeight());
        }

        return object;
    }

}
