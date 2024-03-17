package org.cyclops.cyclopscore.ingredient.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IPrototypedIngredientAlternatives;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeHandler;
import org.cyclops.commoncapabilities.api.capability.recipehandler.RecipeDefinition;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.helper.CraftingHelpers;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A recipe handler that handles {@link RecipeType}.
 * @author rubensworks
 */
public abstract class RecipeHandlerRecipeType<C extends Container, R extends Recipe<C>> implements IRecipeHandler {

    private final Supplier<Level> worldSupplier;
    private final RecipeType<R> recipeType;
    private final Set<IngredientComponent<?, ?>> inputComponents;
    private final Set<IngredientComponent<?, ?>> outputComponents;

    public RecipeHandlerRecipeType(Supplier<Level> worldSupplier, RecipeType<R> recipeType,
                                   Set<IngredientComponent<?, ?>> inputComponents,
                                   Set<IngredientComponent<?, ?>> outputComponents) {
        this.worldSupplier = worldSupplier;
        this.recipeType = recipeType;
        this.inputComponents = inputComponents;
        this.outputComponents = outputComponents;
    }

    @Override
    public Set<IngredientComponent<?, ?>> getRecipeInputComponents() {
        return this.inputComponents;
    }

    @Override
    public Set<IngredientComponent<?, ?>> getRecipeOutputComponents() {
        return this.outputComponents;
    }

    @Override
    public Collection<IRecipeDefinition> getRecipes() {
        return ((Collection<RecipeHolder<R>>) worldSupplier.get().getRecipeManager().byType(recipeType).values()).stream()
                .map(this::getRecipeDefinition)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    public IRecipeDefinition getRecipeDefinition(RecipeHolder<R> recipe) {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = getRecipeInputIngredients(recipe.value());
        if (inputs == null) {
            return null;
        }
        IMixedIngredients outputs = getRecipeOutputIngredients(recipe.value());
        if (outputs == null) {
            return null;
        }
        return new RecipeDefinition(inputs, outputs);
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        C container = getRecipeInputContainer(input);
        if (container == null) {
            return null;
        }
        R recipe = CraftingHelpers.findRecipeCached(recipeType, container, worldSupplier.get(), true).orElse(null);
        if (recipe == null) {
            return null;
        }
        return getRecipeOutputIngredients(recipe);
    }

    /**
     * Create a new container for the given recipe's input.
     * @param recipeInput A recipe input.
     * @return A container.
     */
    @Nullable
    protected abstract C getRecipeInputContainer(IMixedIngredients recipeInput);

    /**
     * Get the input ingredients for the given recipe.
     * @param recipe A recipe.
     * @return Input ingredients.
     */
    @Nullable
    protected abstract Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> getRecipeInputIngredients(R recipe);

    /**
     * Get the output ingredients for the given recipe.
     * @param recipe A recipe.
     * @return Output ingredients.
     */
    @Nullable
    protected abstract IMixedIngredients getRecipeOutputIngredients(R recipe);

}
