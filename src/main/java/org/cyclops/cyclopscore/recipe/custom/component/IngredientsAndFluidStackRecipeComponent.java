package org.cyclops.cyclopscore.recipe.custom.component;

import lombok.ToString;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

import java.util.List;

/**
 * A {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} component that holds {@link ItemStack}s
 * and a {@link FluidStack}.
 * @author immortaleeb
 */
@ToString
public class IngredientsAndFluidStackRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IItemStacksRecipeComponent, IFluidStackRecipeComponent {
    private final IngredientsRecipeComponent ingredient;
    private final FluidStackRecipeComponent fluidStack;
    private float chance;

    public IngredientsAndFluidStackRecipeComponent(NonNullList<Ingredient> ingredient, FluidStack fluidStack) {
        this.ingredient = new IngredientsRecipeComponent(ingredient);
        this.fluidStack = new FluidStackRecipeComponent(fluidStack);
    }

    public IngredientsAndFluidStackRecipeComponent(List<IngredientRecipeComponent> ingredientComponents, FluidStack fluidStack) {
        this.ingredient = new IngredientsRecipeComponent(ingredientComponents);
        this.fluidStack = new FluidStackRecipeComponent(fluidStack);
    }

    @Override
    public List<ItemStack> getIngredients() {
        return ingredient.getIngredients();
    }

    public FluidStack getFluidStack() {
        return fluidStack.getFluidStack();
    }

    public List<IngredientRecipeComponent> getSubIngredientComponents() {
        return this.ingredient.getSubIngredientComponents();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientsAndFluidStackRecipeComponent)) return false;

        IngredientsAndFluidStackRecipeComponent that = (IngredientsAndFluidStackRecipeComponent) o;

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

    public void setChance(float chance) {
        this.chance = chance;
    }

    @Override
    public float getChance() {
        return this.chance;
    }

    public IngredientsRecipeComponent getIngredientsComponent() {
        return this.ingredient;
    }

    public FluidStackRecipeComponent getFluidStackComponent() {
        return this.fluidStack;
    }
}
