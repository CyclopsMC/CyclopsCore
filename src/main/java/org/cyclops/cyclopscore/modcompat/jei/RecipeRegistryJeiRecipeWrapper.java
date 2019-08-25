package org.cyclops.cyclopscore.modcompat.jei;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.cyclops.cyclopscore.recipe.custom.api.IMachine;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * A base implementation of a super-recipe-based JEI recipe wrapper.
 * This caches all created recipe wrappers so they can be reused (or removed).
 * @param <I> The type of the recipe input of all recipes associated with the machine.
 * @param <O> The type of the recipe output of all recipes associated with the machine.
 * @param <P> The type of the recipe properties of all recipes associated with the machine.
 * @author rubensworks
 */
public abstract class RecipeRegistryJeiRecipeWrapper<M extends IMachine<M, I, O, P>, I extends IRecipeInput,
        O extends IRecipeOutput, P extends IRecipeProperties, T extends RecipeRegistryJeiRecipeWrapper<M, I, O, P, T>> {

    private static final Map<IRecipe<?, ?, ?>, RecipeRegistryJeiRecipeWrapper<?, ?, ?, ?, ?>> RECIPE_WRAPPERS = Maps.newIdentityHashMap();

    protected final IRecipe<I, O, P> recipe;

    protected RecipeRegistryJeiRecipeWrapper(IRecipe<I, O, P> recipe) {
        this.recipe = recipe;
    }

    public IRecipe<I, O, P> getRecipe() {
        return recipe;
    }

    protected abstract IRecipeRegistry<M, I, O, P> getRecipeRegistry();

    protected abstract T newInstance(IRecipe<I, O, P> input);

    public static <M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput,
            P extends IRecipeProperties, T extends RecipeRegistryJeiRecipeWrapper<M, I, O, P, T>>
            T getJeiRecipeWrapper(IRecipe<I, O, P> input) {
        return (T) RECIPE_WRAPPERS.get(input);
    }

    public List<T> createAllRecipes() {
        return Lists.transform(getRecipeRegistry().allRecipes(), new Function<IRecipe<I, O, P>, T>() {
            @Nullable
            @Override
            public T apply(IRecipe<I, O, P> input) {
                if (!RECIPE_WRAPPERS.containsKey(input)) {
                    RECIPE_WRAPPERS.put(input, newInstance(input));
                }
                return (T) RECIPE_WRAPPERS.get(input);
            }
        });
    }

}
