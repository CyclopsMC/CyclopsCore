package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

/**
 * Furnace recipe exporter.
 */
public class RegistryExportableAbstractCookingRecipe<T extends RecipeType<? extends AbstractCookingRecipe>> extends RegistryExportableRecipeAbstract<T, AbstractCookingRecipe, SingleRecipeInput> {

    protected RegistryExportableAbstractCookingRecipe(Supplier<T> recipeType) {
        super(recipeType);
    }

    @Override
    public JsonObject serializeRecipe(RecipeHolder<AbstractCookingRecipe> recipe) {
        JsonObject object = new JsonObject();
        JsonArray variants = new JsonArray();
        for (Ingredient ingredient : recipe.value().getIngredients()) {
            for (ItemStack matchingStack : ingredient.getItems()) {
                variants.add(IRegistryExportable.serializeItemStack(matchingStack));
            }
        }
        object.add("input", variants);
        object.add("output", IRegistryExportable.serializeItemStack(recipe.value().getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess())));
        return object;
    }

}
