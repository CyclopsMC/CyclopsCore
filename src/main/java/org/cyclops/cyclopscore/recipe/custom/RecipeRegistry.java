package org.cyclops.cyclopscore.recipe.custom;

import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.recipe.custom.api.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link IRecipeRegistry}.
 * @author immortaleeb
 *
 * @param <M> The type of the machine.
 * @param <I> The type of the recipe input of all recipes associated with the machine.
 * @param <O> The type of the recipe output of all recipes associated with the machine.
 * @param <P> The type of the recipe properties of all recipes associated with the machine.
 */
public class RecipeRegistry<M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties>
	implements IRecipeRegistry<M, I, O, P> {
	
    private final M machine;
    private final ModBase mod;

    /**
     * Make a new instance.
     * @param machine The machine this registry is for.
     * @param mod     The mod.
     */
    public RecipeRegistry(M machine, ModBase mod) {
        this.machine = machine;
        this.mod = mod;
    }

    @Override
	public IRecipe<I, O, P> registerRecipe(IRecipe<I, O, P> recipe) {
        getMod().getRegistryManager().getRegistry(ISuperRecipeRegistry.class).getRecipes(machine).add(recipe);
        return recipe;
    }

    @Override
    public IRecipe<I, O, P> registerRecipe(String namedId, I input, O output, P properties) {
        return registerRecipe(new Recipe<I, O, P>(namedId, input, output, properties));
    }

    @Override
    public IRecipe<I, O, P> registerRecipe(I input, O output, P properties) {
        return registerRecipe(new Recipe<I, O, P>(input, output, properties));
    }

    @Override
    public IRecipe<I, O, P> unregisterRecipe(IRecipe<I, O, P> recipe) {
        if (getMod().getRegistryManager().getRegistry(ISuperRecipeRegistry.class).getRecipes(machine).remove(recipe)) {
            return recipe;
        }
        return null;
    }

    @Override
    public IRecipe<I, O, P> unregisterRecipe(String namedId, I input, O output, P properties) {
        return unregisterRecipe(new Recipe<I, O, P>(namedId, input, output, properties));
    }

    @Override
    public IRecipe<I, O, P> unregisterRecipe(I input, O output, P properties) {
        return unregisterRecipe(new Recipe<I, O, P>(input, output, properties));
    }

    @Override
    public IRecipe<I, O, P> findRecipeByNamedId(String namedId) {
        return findRecipe(new RecipePropertyMatcher<M, IRecipe<I, O, P>, String>(namedId) {
            @Override
            public String getProperty(M machine, IRecipe<I, O, P> recipe) {
                return recipe.getNamedId();
            }
        });
    }

    @Override
    public IRecipe<I, O, P> findRecipeByInput(I input) {
        return findRecipe(new RecipePropertyMatcher<M, IRecipe<I, O, P>, I>(input) {

            @Override
            public I getProperty(M machine, IRecipe<I, O, P> recipe) {
                return recipe.getInput();
            }
        });
    }

    @Override
    public List<IRecipe<I, O, P>> findRecipesByInput(I input) {
        return findRecipes(new RecipePropertyMatcher<M, IRecipe<I, O, P>, I>(input) {

            @Override
            public I getProperty(M machine, IRecipe<I, O, P> recipe) {
                return recipe.getInput();
            }
        });
    }

    @Override
    public IRecipe<I, O, P> findRecipeByOutput(O output) {
        return findRecipe(new RecipePropertyMatcher<M, IRecipe<I, O, P>, O>(output) {

            @Override
            public O getProperty(M machine, IRecipe<I, O, P> recipe) {
                return recipe.getOutput();
            }
        });
    }

    @Override
    public List<IRecipe<I, O, P>> findRecipesByOutput(O output) {
        return findRecipes(new RecipePropertyMatcher<M, IRecipe<I, O, P>, O>(output) {

            @Override
            public O getProperty(M machine, IRecipe<I, O, P> recipe) {
                return recipe.getOutput();
            }
        });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public IRecipe<I, O, P> findRecipe(IRecipeMatcher<M, IRecipe<I, O, P>> recipeMatcher) {
        for (IRecipe r : getMod().getRegistryManager().getRegistry(ISuperRecipeRegistry.class).getRecipes(machine)) {
            IRecipe<I, O, P> recipe = r;
            if (recipeMatcher.matches(machine, recipe))
                return recipe;
        }

        return null;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<IRecipe<I, O, P>> findRecipes(IRecipeMatcher<M, IRecipe<I, O, P>> recipeMatcher) {
        List<IRecipe<I, O, P>> results = new ArrayList<IRecipe<I, O, P>>();

        for (IRecipe r : getMod().getRegistryManager().getRegistry(ISuperRecipeRegistry.class).getRecipes(machine)) {
            IRecipe<I, O, P> recipe = r;
            if (recipeMatcher.matches(machine, recipe))
                results.add(recipe);
        }

        return results;
    }

    @Override
    public List<IRecipe<I, O, P>> allRecipes() {
        return (List) getMod().getRegistryManager().getRegistry(ISuperRecipeRegistry.class).getRecipes(machine);
    }

    @Override
    public ModBase getMod() {
        return this.mod;
    }
}