package org.cyclops.cyclopscore.capability.fluid;

import net.minecraftforge.fluids.capability.IFluidHandlerItem;

/**
 * An item fluid handler with a mutable capacity.
 * @author rubensworks
 */
public interface IFluidHandlerItemCapacity extends IFluidHandlerItem {

    public void setCapacity(int capacity);
    public int getCapacity();

}
