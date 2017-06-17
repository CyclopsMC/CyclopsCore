package org.cyclops.cyclopscore.modcompat.jei;

import mezz.jei.api.recipe.IRecipeWrapper;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

/**
 * A JEI recipe wrapper wrapper.
 * @author rubensworks
 */
public interface IJeiRecipeWrapperWrapper<I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> {

    public IRecipeWrapper wrap(IRecipe<I, O, P> recipe);

}
