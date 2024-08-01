package org.cyclops.commoncapabilities.api.ingredient;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.capability.recipehandler.IRecipeDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Raw implementation of mixed ingredients.
 * @author rubensworks
 */
public class MixedIngredients extends MixedIngredientsAdapter {

    private final Map<IngredientComponent<?, ?>, List<?>> ingredients;

    public MixedIngredients(Map<IngredientComponent<?, ?>, List<?>> ingredients) {
        this.ingredients = ingredients;

        // Ensure that the lists are non-empty
        for (Map.Entry<IngredientComponent<?, ?>, List<?>> entry : this.ingredients.entrySet()) {
            if (entry.getValue().isEmpty()) {
                throw new IllegalArgumentException(String.format("Invalid MixedIngredients input, empty list for %s",
                        entry.getKey().getName()));
            }
        }
    }

    @Override
    public Set<IngredientComponent<?, ?>> getComponents() {
        return ingredients.keySet();
    }

    @Override
    public <T> List<T> getInstances(IngredientComponent<T, ?> ingredientComponent) {
        return (List<T>) ingredients.getOrDefault(ingredientComponent, Collections.emptyList());
    }

    public static <T> MixedIngredients ofInstances(IngredientComponent<T, ?> component, Collection<T> instances) {
        Map<IngredientComponent<?, ?>, List<?>> ingredients = Maps.newIdentityHashMap();
        ingredients.put(component, instances instanceof ArrayList ? (List<?>) instances : Lists.newArrayList(instances));
        return new MixedIngredients(ingredients);
    }

    /**
     * Create a new ingredients for a single instance.
     * @param component A component type.
     * @param instance An instance.
     * @param <T> The instance type.
     * @return New ingredients.
     */
    public static <T> MixedIngredients ofInstance(IngredientComponent<T, ?> component, T instance) {
        return ofInstances(component, Lists.newArrayList(instance));
    }

    /**
     * Create ingredients from the given recipe's input.
     * This will create the ingredients based on the first prototype of the recipe's inputs.
     * @param recipe A recipe.
     * @return New ingredients.
     */
    public static MixedIngredients fromRecipeInput(IRecipeDefinition recipe) {
        Map<IngredientComponent<?, ?>, List<?>> ingredients = Maps.newIdentityHashMap();
        for (IngredientComponent<?, ?> component : recipe.getInputComponents()) {
            IIngredientMatcher<?, ?> matcher = component.getMatcher();
            List<?> instances = recipe.getInputs(component).stream()
                    .map(ingredient -> {
                        IPrototypedIngredient<?, ?> firstIngredient = Iterables.getFirst(ingredient.getAlternatives(), null);
                        if (firstIngredient == null) {
                            return matcher.getEmptyInstance();
                        }
                        return firstIngredient.getPrototype();
                    })
                    .collect(Collectors.toList());
            if (!instances.isEmpty()) {
                ingredients.put(component, instances);
            }
        }
        return new MixedIngredients(ingredients);
    }
}
