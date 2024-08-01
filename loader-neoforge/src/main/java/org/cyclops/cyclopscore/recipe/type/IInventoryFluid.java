package org.cyclops.cyclopscore.recipe.type;

import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * An inventory that can also contain fluids.
 * @author rubensworks
 */
public interface IInventoryFluid extends RecipeInput {

    public IFluidHandler getFluidHandler();

}
