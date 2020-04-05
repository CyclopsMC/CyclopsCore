package org.cyclops.cyclopscore.recipe.type;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * An inventory that can also contain fluids.
 * @author rubensworks
 */
public interface IInventoryFluid extends IInventory {

    public IFluidHandler getFluidHandler();

}
