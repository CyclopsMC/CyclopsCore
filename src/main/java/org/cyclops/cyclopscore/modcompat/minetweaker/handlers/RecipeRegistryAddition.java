package org.cyclops.cyclopscore.modcompat.minetweaker.handlers;

import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.common.Optional;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.modcompat.jei.IJeiRecipeWrapperWrapper;
import org.cyclops.cyclopscore.recipe.custom.api.*;

/**
 * Generic CyclopsCore {@link IRecipeRegistry} recipe addition class.
 * @author rubensworks
 */
public class RecipeRegistryAddition
        <M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties>
        extends BaseListAddition<IRecipe<I, O, P>> {

    private final M machine;
    private final IJeiRecipeWrapperWrapper<I, O, P> jeiRecipeWrapperWrapper;

    protected RecipeRegistryAddition(String machineName, M machine, IRecipe<I, O, P> recipe,
                                     IJeiRecipeWrapperWrapper<I, O, P> jeiRecipeWrapperWrapper) {
        super(machineName, machine.getRecipeRegistry().allRecipes());
        this.machine = machine;
        this.recipes.add(recipe);
        this.jeiRecipeWrapperWrapper = jeiRecipeWrapperWrapper;
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
                    MineTweakerAPI.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                } else {
                    MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(wrapRecipe(recipe) != null ? wrapRecipe(recipe) : recipe);
                }
            } else {
                MineTweakerAPI.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
    }

    @Optional.Method(modid = Reference.MOD_JEI)
    @Override
    public mezz.jei.api.recipe.IRecipeWrapper wrapRecipe(IRecipe<I, O, P> recipe) {
        return jeiRecipeWrapperWrapper.wrap(recipe);
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
