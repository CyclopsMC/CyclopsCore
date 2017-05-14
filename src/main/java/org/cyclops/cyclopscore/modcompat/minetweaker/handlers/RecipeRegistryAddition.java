package org.cyclops.cyclopscore.modcompat.minetweaker.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import org.cyclops.cyclopscore.recipe.custom.api.*;

/**
 * Generic CyclopsCore {@link IRecipeRegistry} recipe addition class.
 * @author rubensworks
 */
public class RecipeRegistryAddition
        <M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties>
        extends BaseListAddition<IRecipe<I, O, P>> {

    private final M machine;

    protected RecipeRegistryAddition(String machineName, M machine, IRecipe<I, O, P> recipe) {
        super(machineName, machine.getRecipeRegistry().allRecipes());
        this.machine = machine;
        this.recipes.add(recipe);
    }

    @Override
    protected String getRecipeInfo(IRecipe<I, O, P> recipe) {
        return getRecipeInfo(this.name, recipe);
    }

    @Override
    public void undo() {
        for (IRecipe recipe : this.successful) {
            if (recipe != null) {
                if (machine.getRecipeRegistry().unregisterRecipe(recipe) == null) {
                    LogHelper.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
    }

    public static <I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties>
    String getRecipeInfo(String machineName, IRecipe<I, O, P> recipe) {
        StringBuilder build = new StringBuilder();
        build.append(machineName);
        build.append(" recipe: ");
        build.append("Input: " + recipe.getInput().toString()).append("; ");
        build.append("Output: " + recipe.getOutput().toString()).append("; ");
        build.append("Properties: " + recipe.getProperties().toString()).append("; ");
        return build.toString();
    }

}
