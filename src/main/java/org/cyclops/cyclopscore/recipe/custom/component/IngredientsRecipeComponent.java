package org.cyclops.cyclopscore.recipe.custom.component;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.Data;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} component (input, output or properties) that holds
 * a list of {@link ItemStack}s.
 *
 * @author immortaleeb
 */
@Data
public class IngredientsRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IItemStacksRecipeComponent {

    private final List<IngredientRecipeComponent> ingredients;
    private float chance;

    public IngredientsRecipeComponent(NonNullList<Ingredient> ingredients) {
        this.ingredients = Lists.transform(ingredients, new Function<Ingredient, IngredientRecipeComponent>() {
            @Nullable
            @Override
            public IngredientRecipeComponent apply(Ingredient input) {
                return new IngredientRecipeComponent(input);
            }
        });
    }

    public IngredientsRecipeComponent(List<IngredientRecipeComponent> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IngredientsRecipeComponent)) return false;
        IngredientsRecipeComponent that = (IngredientsRecipeComponent)object;
        return equals(this.ingredients, that.ingredients);
    }

    protected boolean equals(List<IngredientRecipeComponent> a, List<IngredientRecipeComponent> b) {
        if (a.size() == b.size()) {
            for (int i = 0; i < a.size(); i++) {
                if (!a.get(i).equals(b.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 876;
        for (IngredientRecipeComponent itemStack : ingredients) {
            hash |= itemStack.hashCode();
        }
        return hash;
    }

    public List<ItemStack> getIngredients() {
        return ingredients.stream()
                .map(IngredientRecipeComponent::getItemStacks)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<IngredientRecipeComponent> getSubIngredientComponents() {
        return this.ingredients;
    }
}
