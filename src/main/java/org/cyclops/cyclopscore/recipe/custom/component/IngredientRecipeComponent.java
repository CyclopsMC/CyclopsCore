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
public class IngredientRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IIngredientRecipeComponent {

    private static final int META_WILDCARD = OreDictionary.WILDCARD_VALUE;

    private final Ingredient ingredient;
    private float chance;

    public IngredientRecipeComponent(Ingredient ingredient) {
        this.ingredient = Objects.requireNonNull(ingredient);
    }

    public IngredientRecipeComponent(ItemStack itemStack) {
        this(Ingredient.fromStacks(itemStack));
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IIngredientRecipeComponent)) return false;
        IIngredientRecipeComponent that = (IIngredientRecipeComponent)object;

        if (this.getIngredient() == Ingredient.EMPTY && that.getIngredient() == Ingredient.EMPTY) {
            return true;
        }

        for (ItemStack itemStack : getItemStacks()) {
            if (that.getIngredient().apply(itemStack.isEmpty() ? null : itemStack)) {
                return true;
            }
        }
        if (getIngredient().apply(that.getFirstItemStack())) {
            return true;
        }
        return false;
    }

    protected boolean equals(ItemStack a, ItemStack b) {
        return (a.isEmpty() && b.isEmpty()) ||
                (!a.isEmpty() && !b.isEmpty() && a.getItem().equals(b.getItem()) &&
                        (a.getItemDamage() == b.getItemDamage() ||
                                a.getItemDamage() == META_WILDCARD || b.getItemDamage() == META_WILDCARD));
    }

    @Override
    public int hashCode() {
        return getItemStacks().stream().map(ItemStack::getItem).mapToInt(Object::hashCode).sum() + 876;
    }

    public List<ItemStack> getItemStacks() {
        return Lists.newArrayList(getIngredient().getMatchingStacks());
    }

    @Override
    public Ingredient getIngredient() {
        return this.ingredient;
    }

    @Override
    public ItemStack getFirstItemStack() {
        ItemStack[] itemStacks = this.ingredient.getMatchingStacks();
        return itemStacks.length > 0 ? itemStacks[0] : ItemStack.EMPTY;
    }
}
