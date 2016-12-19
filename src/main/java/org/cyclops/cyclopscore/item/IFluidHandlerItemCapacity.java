package org.cyclops.cyclopscore.item;

import net.minecraftforge.fluids.capability.IFluidHandlerItem;

/**
 * An ItemStack fluid handler with a mutable capacity.
 * @author rubensworks
 */
public interface IFluidHandlerItemCapacity extends IFluidHandlerItem {

    public void setCapacity(int capacity);
    public int getCapacity();

}
