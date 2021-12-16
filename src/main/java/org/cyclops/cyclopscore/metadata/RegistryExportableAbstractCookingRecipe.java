package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

/**
 * Furnace recipe exporter.
 */
public class RegistryExportableAbstractCookingRecipe<T extends RecipeType<? extends AbstractCookingRecipe>> extends RegistryExportableRecipeAbstract<T, AbstractCookingRecipe, Container> {

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
