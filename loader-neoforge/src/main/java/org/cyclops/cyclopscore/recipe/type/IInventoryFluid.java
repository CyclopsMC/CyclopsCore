package org.cyclops.cyclopscore.recipe.type;

import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

/**
 * An inventory that can also contain fluids.
 * @author rubensworks
 */
public interface IInventoryFluid extends RecipeInput {

    public ResourceHandler<FluidResource> getFluidHandler();

}
