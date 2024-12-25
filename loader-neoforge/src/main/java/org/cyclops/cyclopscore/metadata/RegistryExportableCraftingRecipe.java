package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;

/**
 * Crafting recipe exporter.
 */
public class RegistryExportableCraftingRecipe extends RegistryExportableRecipeAbstract<RecipeType<CraftingRecipe>, CraftingRecipe, CraftingInput> {

    protected RegistryExportableCraftingRecipe() {
        super(() -> RecipeType.CRAFTING);
    }

    @Override
    public JsonObject serializeRecipe(RecipeHolder<CraftingRecipe> recipe) {
        JsonObject object = new JsonObject();

        List<Ingredient> inputs = recipe.value().placementInfo().ingredients();
        JsonArray arrayInputs = new JsonArray();
        for (Ingredient input : inputs) {
            JsonArray arrayInputAlternatives = new JsonArray();
            for (Holder<Item> inputAlternative : input.getValues()) {
                arrayInputAlternatives.add(IRegistryExportable.serializeItemStack(new ItemStack(inputAlternative.value())));
            }
            arrayInputs.add(arrayInputAlternatives);
        }
        object.addProperty("id", recipe.id().toString());
        object.add("input", arrayInputs);
        object.add("output", IRegistryExportable.serializeItemStack(IRegistryExportable.getRecipeOutput(recipe)));

        if(recipe.value() instanceof ShapedRecipe) {
            object.addProperty("width", ((ShapedRecipe) recipe.value()).getWidth());
            object.addProperty("height", ((ShapedRecipe) recipe.value()).getHeight());
        }

        return object;
    }

}
