package org.cyclops.cyclopscore.recipe.custom.component;

import lombok.ToString;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

import java.util.List;

/**
 * A {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} component that holds an {@link net.minecraft.item.ItemStack}
 * and a {@link net.minecraftforge.fluids.FluidStack}.
 * @author immortaleeb
 */
@ToString
public class IngredientAndFluidStackRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IIngredientRecipeComponent, IFluidStackRecipeComponent {
    private final IngredientRecipeComponent ingredient;
    private final FluidStackRecipeComponent fluidStack;

    public IngredientAndFluidStackRecipeComponent(Ingredient ingredient, FluidStack fluidStack) {
        this.ingredient = new IngredientRecipeComponent(ingredient);
        this.fluidStack = new FluidStackRecipeComponent(fluidStack);
    }

    public IngredientAndFluidStackRecipeComponent(ItemStack itemStack, FluidStack fluidStack) {
        this.ingredient = new IngredientRecipeComponent(itemStack);
        this.fluidStack = new FluidStackRecipeComponent(fluidStack);
    }

    @Override
    public Ingredient getIngredient() {
        return ingredient.getIngredient();
    }

    @Override
    public ItemStack getFirstItemStack() {
        return ingredient.getFirstItemStack();
    }

    public List<ItemStack> getItemStacks() {
        return ingredient.getItemStacks();
    }

    public FluidStack getFluidStack() {
        return fluidStack.getFluidStack();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientAndFluidStackRecipeComponent)) return false;

        IngredientAndFluidStackRecipeComponent that = (IngredientAndFluidStackRecipeComponent) o;

        if (!fluidStack.equals(that.fluidStack)) return false;
        if (!ingredient.equals(that.ingredient)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ingredient.hashCode();
        result = 31 * result + fluidStack.hashCode();
        return result;
    }
}
