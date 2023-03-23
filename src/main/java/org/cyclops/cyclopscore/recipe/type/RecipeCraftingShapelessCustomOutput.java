package org.cyclops.cyclopscore.recipe.type;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

/**
 * @author rubensworks
 */
public class RecipeCraftingShapelessCustomOutput extends ShapelessRecipe {

    private final RecipeSerializerCraftingShapelessCustomOutput serializer;
    private final ItemStack recipeOutput;

    public RecipeCraftingShapelessCustomOutput(RecipeSerializerCraftingShapelessCustomOutput serializer, ResourceLocation idIn, String groupIn, CraftingBookCategory category, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, category, recipeOutputIn, recipeItemsIn);
        this.serializer = serializer;
        this.recipeOutput = recipeOutputIn;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        RecipeSerializerCraftingShapelessCustomOutput.IOutputTransformer outputTransformer = serializer.getOutputTransformer();
        if (outputTransformer != null) {
            return outputTransformer.transform(inv, this.getResultItem());
        }
        return this.getResultItem().copy();
    }

    public ItemStack getResultItem() {
        return this.recipeOutput;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return getResultItem();
    }
}
