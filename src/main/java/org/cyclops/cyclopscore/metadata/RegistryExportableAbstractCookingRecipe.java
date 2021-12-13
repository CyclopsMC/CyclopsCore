package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Supplier;

/**
 * Furnace recipe exporter.
 */
public class RegistryExportableAbstractCookingRecipe<T extends IRecipeType<? extends AbstractCookingRecipe>> extends RegistryExportableRecipeAbstract<T, AbstractCookingRecipe, IInventory> {

    protected RegistryExportableAbstractCookingRecipe(Supplier<T> recipeType) {
        super(recipeType);
    }

    @Override
    public JsonObject serializeRecipe(AbstractCookingRecipe recipe) {
        JsonObject object = new JsonObject();
        JsonArray variants = new JsonArray();
        for (Ingredient ingredient : recipe.getIngredients()) {
            for (ItemStack matchingStack : ingredient.getItems()) {
                variants.add(IRegistryExportable.serializeItemStack(matchingStack));
            }
        }
        object.add("input", variants);
        object.add("output", IRegistryExportable.serializeItemStack(recipe.getResultItem()));
        return object;
    }

}
