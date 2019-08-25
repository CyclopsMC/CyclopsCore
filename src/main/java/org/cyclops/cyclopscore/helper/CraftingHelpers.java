package org.cyclops.cyclopscore.helper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

/**
 * Several convenience functions for crafting.
 * @author rubensworks
 */
public class CraftingHelpers {

    public static <C extends IInventory, T extends IRecipe<C>> IRecipe<C> findRecipe(ItemStack itemStack, IRecipeType<T> recipeType, int index) throws IllegalArgumentException {
        int indexAttempt = index;
        for(IRecipe<C> recipe : ServerLifecycleHooks.getCurrentServer().getRecipeManager().getRecipes(recipeType).values()) {
            if(ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), itemStack) && indexAttempt-- == 0) {
                return recipe;
            }
        }
        throw new IllegalArgumentException("Could not find recipe for " + itemStack + "::"
                + itemStack.getTag() + " with index " + index);
    }

    @Deprecated // TODO: rm
    public static ResourceLocation newRecipeIdentifier(ItemStack output) {
        ResourceLocation id = new ResourceLocation(ModLoadingContext.get().getActiveContainer().getModId(),
                output.getItem().getRegistryName().getPath());
        int counter = 10;
        while (ServerLifecycleHooks.getCurrentServer().getRecipeManager().getRecipe(id).isPresent()) {
            id = new ResourceLocation(id.getNamespace(), output.getItem().getRegistryName().getPath() + "_" + ++counter);
        }
        return id;
    }

    /**
     * Register a crafting recipe.
     * @param id The recipe id
     * @param recipe The recipe
     * @return The recipe
     */
    @Deprecated // TODO: rm
    public static IRecipe<?> registerRecipe(ResourceLocation id, IRecipe<?> recipe) {
        /*recipe.setRegistryName(id);
        ForgeRegistries.RECIPES.register(recipe);*/
        return recipe;
    }

}
