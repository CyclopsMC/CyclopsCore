package org.cyclops.cyclopscore.ingredient.recipe;

import com.google.common.collect.Maps;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;

import javax.annotation.Nullable;
import java.util.Map;

public class RecipeInputOutputDefinitionRegistry implements IRecipeInputOutputDefinitionRegistry {

    private final Map<Class<?>, IRecipeInputDefinitionHandler<?>> inputHandlers = Maps.newIdentityHashMap();
    private final Map<Class<?>, IRecipeOutputDefinitionHandler<?>> outputHandlers = Maps.newIdentityHashMap();

    @Override
    public <T extends IRecipeInput> void setRecipeInputHandler(Class<? extends T> clazz, IRecipeInputDefinitionHandler<T> handler) {
        inputHandlers.put(clazz, handler);
    }

    @Override
    public <T extends IRecipeOutput> void setRecipeOutputHandler(Class<? extends T> clazz, IRecipeOutputDefinitionHandler<T> handler) {
        outputHandlers.put(clazz, handler);
    }

    @Nullable
    @Override
    public <T extends IRecipeInput> IRecipeInputDefinitionHandler<T> getRecipeInputHandler(Class<? extends T> clazz) {
        return (IRecipeInputDefinitionHandler<T>) inputHandlers.get(clazz);
    }

    @Nullable
    @Override
    public <T extends IRecipeOutput> IRecipeOutputDefinitionHandler<T> getRecipeOutputHandler(Class<? extends T> clazz) {
        return (IRecipeOutputDefinitionHandler<T>) outputHandlers.get(clazz);
    }
}
