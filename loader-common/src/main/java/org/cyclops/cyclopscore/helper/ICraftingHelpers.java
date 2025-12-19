package org.cyclops.cyclopscore.helper;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;

/**
 * @author rubensworks
 */
public interface ICraftingHelpers {

    public RecipeManager getRecipeManager();

    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> getRecipe(RecipeType<T> recipeType, ResourceKey<Recipe<?>> recipeName);

    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> findRecipe(RecipeType<T> recipeType, C container, Level world);

    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> findRecipes(RecipeType<? extends T> recipeType);

    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> findRecipes(ServerLevel world, RecipeType<? extends T> recipeType);

    public List<RecipeDisplayEntry> getRecipeDisplays(RecipeType<?> recipeType, ResourceKey<Recipe<?>> recipeName);

    public List<Pair<Identifier, RecipeDisplayEntry>> getRecipeDisplays(RecipeType<?> recipeType, String recipeNameRegex);

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
