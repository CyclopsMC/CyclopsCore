package org.cyclops.cyclopscore.modcompat.jei;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import org.cyclops.cyclopscore.helper.CraftingHelpers;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * A base implementation of a recipe-based JEI recipe wrapper.
 * This caches all created recipe wrappers so they can be reused (or removed).
 * @param <C> The type of the recipe container.
 * @param <R> The type of the recipe instance.
 * @author rubensworks
 */
public abstract class RecipeRegistryJeiRecipeWrapper<C extends IInventory, R extends IRecipe<C>,
        J extends RecipeRegistryJeiRecipeWrapper<C, R, J>> {

    private static final Map<IRecipe<?>, RecipeRegistryJeiRecipeWrapper<?, ?, ?>> RECIPE_WRAPPERS = Maps.newIdentityHashMap();

    protected final R recipe;

    protected RecipeRegistryJeiRecipeWrapper(IRecipeType<R> recipeType, R recipe) {
        this.recipe = recipe;
    }

    public R getRecipe() {
        return recipe;
    }

    protected abstract IRecipeType<R> getRecipeType();

    protected abstract J newInstance(R input);

    public static <T extends IRecipeType<R>, C extends IInventory, R extends IRecipe<C>,
            J extends RecipeRegistryJeiRecipeWrapper<C, R, J>> T getJeiRecipeWrapper(R input) {
        return (T) RECIPE_WRAPPERS.get(input);
    }

    public Collection<J> createAllRecipes() {
        return Collections2.transform(CraftingHelpers.getClientRecipes(getRecipeType()), new Function<R, J>() {
            @Nullable
            @Override
            public J apply(R input) {
                if (!RECIPE_WRAPPERS.containsKey(input)) {
                    RECIPE_WRAPPERS.put(input, newInstance(input));
                }
                return (J) RECIPE_WRAPPERS.get(input);
            }
        });
    }

}
