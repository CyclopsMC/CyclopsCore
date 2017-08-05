package org.cyclops.cyclopscore.modcompat.crafttweaker.handlers;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IngredientStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.recipe.custom.api.*;

/**
 * Main handler for the Cyclops {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry}.
 * @author rubensworks
 */
public abstract class RecipeRegistryHandler
        <M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> {

    protected abstract M getMachine();
    protected abstract String getRegistryName();

    public void add(IRecipe<I, O, P> recipe) {
        CraftTweakerAPI.apply(new RecipeRegistryAddition<>(getRegistryName(), getMachine(), recipe));
    }

    public void remove(IRecipe<I, O, P> recipe) {
        CraftTweakerAPI.apply(new RecipeRegistryRemoval<>(getRegistryName(), getMachine(), recipe));
    }

    public void remove(O output) {
        CraftTweakerAPI.apply(new RecipeRegistryRemoval<>(getRegistryName(), getMachine(), output));
    }

    public static ItemStack toStack(IItemStack stack) {
        if (stack == null) {
            return ItemStack.EMPTY;
        } else {
            Object internal = stack.getInternal();
            if (!(internal instanceof ItemStack)) {
                CyclopsCore.clog("Not a valid item stack: " + stack);
                return null;
            }
            return (ItemStack) internal;
        }
    }

    public static Ingredient toIngredient(IIngredient ingredient) {
        if (ingredient == null) {
            return Ingredient.EMPTY;
        } else {
            if(ingredient instanceof IOreDictEntry) {
                return new OreIngredient(((IOreDictEntry) ingredient).getName());
            } else if(ingredient instanceof IItemStack) {
                return Ingredient.fromStacks(toStack((IItemStack) ingredient));
            } else if(ingredient instanceof IngredientStack) {
                return Ingredient.fromStacks(((IngredientStack) ingredient).getItems().stream()
                        .map(RecipeRegistryHandler::toStack).toArray(ItemStack[]::new));
            } else {
                CyclopsCore.clog("Not a valid ingredient: " + ingredient);
                return Ingredient.EMPTY;
            }
        }
    }

    public static FluidStack toFluid(ILiquidStack stack) {
        if (stack == null) {
            return null;
        } else {
            return FluidRegistry.getFluidStack(stack.getName(), stack.getAmount());
        }
    }

}
