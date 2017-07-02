package org.cyclops.cyclopscore.recipe.custom.component;

import com.google.common.collect.Lists;
import lombok.Data;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

import java.util.List;
import java.util.Objects;

/**
 * A {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} component (input, output or properties) that holds an
 * {@link net.minecraft.item.ItemStack}.
 *
 * @author immortaleeb
 */
@Data
public class IngredientRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IItemStackRecipeComponent {

    private static final int META_WILDCARD = OreDictionary.WILDCARD_VALUE;

    private final Ingredient ingredient;

    public IngredientRecipeComponent(Ingredient ingredient) {
        this.ingredient = Objects.requireNonNull(ingredient);
    }

    public IngredientRecipeComponent(ItemStack itemStack) {
        this(Ingredient.fromStacks(itemStack));
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IngredientRecipeComponent)) return false;
        IngredientRecipeComponent that = (IngredientRecipeComponent)object;

        // To increase performance, first check if the comparing stack is not null before
        // potentially matching it with the whole oredict.
        if (that.getIngredient() != null) {
            for (ItemStack itemStack : getItemStacks()) {
                if (equals(itemStack, that.getIngredient())) {
                    return true;
                }
            }
            return false;
        }

        return getItemStacks().isEmpty();
    }

    protected boolean equals(ItemStack a, ItemStack b) {
        return (a.isEmpty() && b.isEmpty()) ||
                (!a.isEmpty() && !b.isEmpty() && a.getItem().equals(b.getItem()) &&
                        (a.getItemDamage() == b.getItemDamage() ||
                                a.getItemDamage() == META_WILDCARD || b.getItemDamage() == META_WILDCARD));
    }

    @Override
    public int hashCode() {
        return !getIngredient().isEmpty() ? getIngredient().getItem().hashCode() + 876 : 0;
    }

    public List<ItemStack> getItemStacks() {
        return Lists.newArrayList(getIngredient());
    }

    @Override
    public ItemStack getIngredient() {
        return this.ingredient.getMatchingStacks().length > 0 ? this.ingredient.getMatchingStacks()[0] : ItemStack.EMPTY;
    }
}
