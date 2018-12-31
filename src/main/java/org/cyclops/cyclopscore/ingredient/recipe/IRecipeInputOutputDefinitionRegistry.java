package org.cyclops.cyclopscore.ingredient.recipe;

import org.cyclops.cyclopscore.init.IRegistry;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;

import javax.annotation.Nullable;

/**
 * Regsitry for handlers for for the conversion between
 * {@link org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition}
 * and {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe}.
 */
public interface IRecipeInputOutputDefinitionRegistry extends IRegistry {

    public <T extends IRecipeInput> void setRecipeInputHandler(Class<? extends T> clazz, IRecipeInputDefinitionHandler<T> handler);

    public <T extends IRecipeOutput> void setRecipeOutputHandler(Class<? extends T> clazz, IRecipeOutputDefinitionHandler<T> handler);

    public default <T extends IRecipeInput & IRecipeOutput, H extends IRecipeInputDefinitionHandler<T> & IRecipeOutputDefinitionHandler<T>>
    void setRecipeInputOutputHandler(Class<? extends T> clazz, H handler) {
        setRecipeInputHandler(clazz, handler);
        setRecipeOutputHandler(clazz, handler);
    }

    @Nullable
    public <T extends IRecipeInput> IRecipeInputDefinitionHandler<T> getRecipeInputHandler(Class<? extends T> clazz);

    @Nullable
    public <T extends IRecipeOutput> IRecipeOutputDefinitionHandler<T> getRecipeOutputHandler(Class<? extends T> clazz);

}
