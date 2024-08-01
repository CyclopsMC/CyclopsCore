package org.cyclops.commoncapabilities.api.capability.fluidhandler;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A filtered iterator over all slots in a fluid handler.
 * @author rubensworks
 */
public class FilteredFluidHandlerFluidStackIterator implements Iterator<FluidStack> {

    private final IFluidHandler fluidHandler;
    private final FluidStack prototype;
    private final int matchFlags;
    private int slot = 0;
    private FluidStack next;

    public FilteredFluidHandlerFluidStackIterator(IFluidHandler fluidHandler, FluidStack prototype, int matchFlags) {
        this.fluidHandler = fluidHandler;
        this.prototype = prototype;
        this.matchFlags = matchFlags;
        this.next = findNext();
    }

    protected FluidStack findNext() {
        while(slot < fluidHandler.getTanks()) {
            FluidStack fluidStack = fluidHandler.getFluidInTank(slot++);
            if (FluidMatch.areFluidStacksEqual(fluidStack, prototype, matchFlags)) {
                return fluidStack;
            }
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public FluidStack next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Slot out of bounds");
        }
        FluidStack next = this.next;
        this.next = findNext();
        return next;
    }
}
