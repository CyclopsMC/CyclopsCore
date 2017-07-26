package org.cyclops.cyclopscore.modcompat.minetweaker.handlers;

import mezz.jei.api.recipe.IRecipeWrapper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.modcompat.jei.IJeiRecipeWrapperWrapper;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.cyclopscore.recipe.custom.api.*;

/**
 * Main handler for the Cyclops {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry}.
 * @author rubensworks
 */
public abstract class RecipeRegistryHandler
        <M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> {

    protected abstract M getMachine();
    protected abstract String getRegistryName();

    @Optional.Method(modid = Reference.MOD_JEI)
    protected IJeiRecipeWrapperWrapper<I, O, P> createJeiWrapperWrapper() {
        return new IJeiRecipeWrapperWrapper<I, O, P>() {
            @Override
            public IRecipeWrapper wrap(IRecipe<I, O, P> recipe) {
                return RecipeRegistryJeiRecipeWrapper.getJeiRecipeWrapper(recipe);
            }
        };
    }

    protected IJeiRecipeWrapperWrapper<I, O, P> createJeiWrapperWrapperSafe() {
        if (!Loader.isModLoaded(Reference.MOD_JEI)) {
            return null;
        }
        return createJeiWrapperWrapper();
    }

    public void add(IRecipe<I, O, P> recipe) {
        MineTweakerAPI.apply(new RecipeRegistryAddition<>(getRegistryName(), getMachine(), recipe,
                createJeiWrapperWrapperSafe()));
    }

    public void remove(IRecipe<I, O, P> recipe) {
        MineTweakerAPI.apply(new RecipeRegistryRemoval<>(getRegistryName(), getMachine(), recipe,
                createJeiWrapperWrapperSafe()));
    }

    public void remove(O output) {
        MineTweakerAPI.apply(new RecipeRegistryRemoval<>(getRegistryName(), getMachine(), output,
                createJeiWrapperWrapperSafe()));
    }

    public static ItemStack toStack(IItemStack stack) {
        if (stack == null) {
            return null;
        } else {
            Object internal = stack.getInternal();
            if (!(internal instanceof ItemStack)) {
                CyclopsCore.clog("Not a valid item stack: " + stack);
                return null;
            }
            return (ItemStack) internal;
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
