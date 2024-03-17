package org.cyclops.cyclopscore.modcompat.jei;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
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
public abstract class RecipeRegistryJeiRecipeWrapper<C extends Container, R extends Recipe<C>,
        J extends RecipeRegistryJeiRecipeWrapper<C, R, J>> {

    private static final Map<ResourceLocation, RecipeRegistryJeiRecipeWrapper<?, ?, ?>> RECIPE_WRAPPERS = Maps.newHashMap();

    protected final R recipe;

    protected RecipeRegistryJeiRecipeWrapper(RecipeType<R> recipeType, R recipe) {
        this.recipe = recipe;
    }

    public R getRecipe() {
        return recipe;
    }

    protected abstract RecipeType<R> getRecipeType();

    protected abstract J newInstance(RecipeHolder<R> input);

    public static <T extends RecipeType<R>, C extends Container, R extends Recipe<C>,
            J extends RecipeRegistryJeiRecipeWrapper<C, R, J>> T getJeiRecipeWrapper(R input) {
        return (T) RECIPE_WRAPPERS.get(input);
    }

    public Collection<J> createAllRecipes() {
        return Collections2.transform(CraftingHelpers.getClientRecipes(getRecipeType()), new Function<RecipeHolder<R>, J>() {
            @Nullable
            @Override
            public J apply(RecipeHolder<R> input) {
                ResourceLocation id = input.id();
                if (!RECIPE_WRAPPERS.containsKey(id)) {
                    RECIPE_WRAPPERS.put(id, newInstance(input));
                }
                return (J) RECIPE_WRAPPERS.get(id);
            }
        });
    }

}
