package org.cyclops.cyclopscore.helper;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * @author rubensworks
 */
public interface ICraftingHelpers {

    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> findRecipes(Level world, RecipeType<? extends T> recipeType);

    public RecipeManager getRecipeManager();

    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> getServerRecipe(RecipeType<T> recipeType, ResourceLocation recipeName);

    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> findServerRecipe(RecipeType<T> recipeType, C container, Level world);

    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> findServerRecipes(RecipeType<? extends T> recipeType);

    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> findServerRecipes(ServerLevel world, RecipeType<? extends T> recipeType);

    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> getClientRecipe(RecipeType<T> recipeType, ResourceLocation recipeName);

    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> getClientRecipes(RecipeType<? extends T> recipeType);

    public <C extends RecipeInput, T extends Recipe<C>> RecipeHolder<T> findClientRecipe(RegistryAccess registryAccess, ItemStack itemStack, RecipeType<T> recipeType, int index) throws IllegalArgumentException;

    /**
     * A cache-based variant of {@link net.minecraft.world.item.crafting.RecipeManager#getRecipeFor(RecipeType, RecipeInput, Level)}.
     * @param recipeType The recipe type.
     * @param inventoryCrafting The crafting inventory.
     * @param world The world.
     * @param uniqueInventory If inventoryCrafting is a unique instance that can be cached safely.
     *                        Otherwise a deep copy will be taken.
     * @return The optional recipe if one was found.
     * @param <C> The inventory type.
     * @param <T> The recipe type.
     */
    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> findRecipeCached(RecipeType<T> recipeType,
                                                                                                   C inventoryCrafting,
                                                                                                   Level world, boolean uniqueInventory);

}
