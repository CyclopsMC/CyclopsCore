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

/**
 * A {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} component (input, output or properties) that holds
 * a list of {@link ItemStack}s.
 *
 * @author immortaleeb
 */
@Data
public class IngredientsRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IItemStacksRecipeComponent {

    private final List<IngredientRecipeComponent> itemStacks;

    public IngredientsRecipeComponent(NonNullList<Ingredient> ingredients) {
        this.itemStacks = Lists.transform(ingredients, new Function<Ingredient, IngredientRecipeComponent>() {
            @Nullable
            @Override
            public IngredientRecipeComponent apply(Ingredient input) {
                return new IngredientRecipeComponent(input);
            }
        });
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IngredientsRecipeComponent)) return false;
        IngredientsRecipeComponent that = (IngredientsRecipeComponent)object;
        return equals(this.itemStacks, that.itemStacks);
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
        for (IngredientRecipeComponent itemStack : itemStacks) {
            hash |= itemStack.hashCode();
        }
        return hash;
    }

    public List<ItemStack> getItemStacks() {
        return Lists.transform(itemStacks, new Function<IngredientRecipeComponent, ItemStack>() {
            @Nullable
            @Override
            public ItemStack apply(@Nullable IngredientRecipeComponent input) {
                return input.getIngredient();
            }
        });
    }
}
