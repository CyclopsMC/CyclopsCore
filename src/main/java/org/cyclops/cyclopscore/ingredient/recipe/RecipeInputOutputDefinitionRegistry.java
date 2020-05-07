package org.cyclops.cyclopscore.ingredient.recipe;

import com.google.common.collect.Maps;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

import javax.annotation.Nullable;
import java.util.Map;

public class RecipeInputOutputDefinitionRegistry implements IRecipeInputOutputDefinitionRegistry {

    private final Map<IRecipeType<?>, IRecipeDefinitionHandler<?, ?, ?>> handlers = Maps.newIdentityHashMap();

    @Override
    public <T extends IRecipeType<R>, C extends IInventory, R extends IRecipe<C>, H extends IRecipeDefinitionHandler<T, C, R>> void setRecipeHandler(T recipeType, H handler) {
        handlers.put(recipeType, handler);
    }

    @Nullable
    @Override
    public <T extends IRecipeType<R>, C extends IInventory, R extends IRecipe<C>> IRecipeDefinitionHandler<T, C, R> getRecipeHandler(T recipeType) {
        return (IRecipeDefinitionHandler<T, C, R>) handlers.get(recipeType);
    }
}
