package org.cyclops.cyclopscore.recipe.custom;

import org.cyclops.cyclopscore.recipe.custom.api.*;

import java.util.List;

/**
 * Helper class to abstract away some function logic (lambdas would make this way simpler though).
 * It allows you to define a matching criteria and allows you to filter out recipe that match these criteria
 * and those that do not. You do this by implementing the matches() method, which should return true when
 * the criteria of the given recipe are met, or false otherwise.
 */
public abstract class RecipeMatcher<M extends IMachine, R extends IRecipe>
	implements IRecipeMatcher<M, R> {

    @Override
	public IRecipeMatch<M, R> findRecipe(ISuperRecipeRegistry superRecipeRegistry) {
        return superRecipeRegistry.findRecipe(this);
    }

    @Override
	public List<IRecipeMatch<M, R>> findRecipes(ISuperRecipeRegistry superRecipeRegistry) {
        return superRecipeRegistry.findRecipes(this);
    }
}