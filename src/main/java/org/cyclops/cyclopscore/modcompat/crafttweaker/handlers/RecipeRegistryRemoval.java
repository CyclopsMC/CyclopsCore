package org.cyclops.cyclopscore.modcompat.crafttweaker.handlers;

import crafttweaker.CraftTweakerAPI;
import org.cyclops.cyclopscore.recipe.custom.api.*;

/**
 * Generic CyclopsCore {@link IRecipeRegistry} recipe removal class.
 * @author rubensworks
 */
public class RecipeRegistryRemoval
        <M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties>
        extends BaseListRemoval<IRecipe<I, O, P>> {

    private final M machine;

    protected RecipeRegistryRemoval(String machineName, M machine, IRecipe<I, O, P> recipe) {
        super(machineName, machine.getRecipeRegistry().allRecipes());
        this.machine = machine;
        this.recipes.add(recipe);
    }

    protected RecipeRegistryRemoval(String machineName, M machine, O output) {
        super(machineName, machine.getRecipeRegistry().allRecipes());
        this.machine = machine;
        this.recipes.addAll(machine.getRecipeRegistry().findRecipesByOutput(output));
    }

    @Override
    public void apply() {
        if (recipes.isEmpty()) {
            return;
        }

        for (IRecipe<I, O, P> recipe : this.recipes) {
            if (recipe != null) {
                IRecipe<I, O, P> removed = machine.getRecipeRegistry().unregisterRecipe(recipe);
                if (removed != null) {
                    successful.add(removed);
                } else {
                    CraftTweakerAPI.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                CraftTweakerAPI.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
    }

    @Override
    protected String getRecipeInfo(IRecipe<I, O, P> recipe) {
        return RecipeRegistryAddition.getRecipeInfo(this.name, recipe);
    }

}
