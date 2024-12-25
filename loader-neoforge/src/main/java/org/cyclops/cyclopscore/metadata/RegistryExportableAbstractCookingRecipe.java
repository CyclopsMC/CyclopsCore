package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

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
        for (Ingredient ingredient : recipe.value().placementInfo().ingredients()) {
            for (Holder<Item> matchingStack : ingredient.getValues()) {
                variants.add(IRegistryExportable.serializeItemStack(new ItemStack(matchingStack)));
            }
        }
        object.add("input", variants);
        object.add("output", IRegistryExportable.serializeItemStack(IRegistryExportable.getRecipeOutput(recipe)));
        return object;
    }

}
