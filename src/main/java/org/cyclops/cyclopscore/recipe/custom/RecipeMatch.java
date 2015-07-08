package org.cyclops.cyclopscore.recipe.custom;

import lombok.Data;
import org.cyclops.cyclopscore.recipe.custom.api.IMachine;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeMatch;

/**
 * A match for a recipe, holds the recipe and the machine it is associated with.
 * @param <M> The type of the machine.
 * @param <R> The type of the recipe.
 */
@Data
public class RecipeMatch<M extends IMachine, R extends IRecipe> implements IRecipeMatch<M, R> {
    private final M machine;
    private final R recipe;
}