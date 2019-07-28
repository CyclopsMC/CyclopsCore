package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;

import java.util.Map;

/**
 * Furnace recipe exporter.
 */
public class RegistryExportableFurnaceRecipe implements IRegistryExportable {

    @Override
    public JsonObject export() {
        JsonObject root = new JsonObject();
        JsonArray elements = new JsonArray();
        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
            elements.add(serializeRecipe(entry));
        }
        root.add("elements", elements);
        return root;
    }

    @Override
    public String getName() {
        return "furnace_recipe";
    }

    public JsonObject serializeRecipe(Map.Entry<ItemStack, ItemStack> recipe) {
        JsonObject object = new JsonObject();
        JsonArray variants = new JsonArray();
        for (ItemStack variant : ItemStackHelpers.getVariants(recipe.getKey())) {
            variants.add(IRegistryExportable.serializeItemStack(variant));
        }
        object.add("input", variants);
        object.add("output", IRegistryExportable.serializeItemStack(recipe.getValue()));
        return object;
    }

}
