package org.cyclops.cyclopscore.modcompat.minetweaker.handlers;

import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.common.Optional;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.modcompat.jei.IJeiRecipeWrapperWrapper;
import org.cyclops.cyclopscore.recipe.custom.api.*;

/**
 * Generic CyclopsCore {@link IRecipeRegistry} recipe removal class.
 * @author rubensworks
 */
public class RecipeRegistryRemoval
        <M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties>
        extends BaseListRemoval<IRecipe<I, O, P>> {

    private final M machine;
    private final IJeiRecipeWrapperWrapper<I, O, P> jeiRecipeWrapperWrapper;

    protected RecipeRegistryRemoval(String machineName, M machine, IRecipe<I, O, P> recipe,
                                    IJeiRecipeWrapperWrapper<I, O, P> jeiRecipeWrapperWrapper) {
        super(machineName, machine.getRecipeRegistry().allRecipes());
        this.machine = machine;
        this.recipes.add(recipe);
        this.jeiRecipeWrapperWrapper = jeiRecipeWrapperWrapper;
    }

    protected RecipeRegistryRemoval(String machineName, M machine, O output,
                                    IJeiRecipeWrapperWrapper<I, O, P> jeiRecipeWrapperWrapper) {
        super(machineName, machine.getRecipeRegistry().allRecipes());
        this.machine = machine;
        this.recipes.addAll(machine.getRecipeRegistry().findRecipesByOutput(output));
        this.jeiRecipeWrapperWrapper = jeiRecipeWrapperWrapper;
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
                    MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(wrapRecipe(recipe) != null ? wrapRecipe(recipe) : recipe);
                } else {
                    MineTweakerAPI.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                MineTweakerAPI.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
    }

    @Override
    protected String getRecipeInfo(IRecipe<I, O, P> recipe) {
        return RecipeRegistryAddition.getRecipeInfo(this.name, recipe);
    }

    @Optional.Method(modid = Reference.MOD_JEI)
    @Override
    public mezz.jei.api.recipe.IRecipeWrapper wrapRecipe(IRecipe<I, O, P> recipe) {
        return jeiRecipeWrapperWrapper.wrap(recipe);
    }

}
