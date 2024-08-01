package org.cyclops.commoncapabilities.api.capability.fluidhandler;

import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Fluid matching flags.
 * @author rubensworks
 */
public final class FluidMatch {

    /**
     * Convenience value matching any FluidStack.
     */
    public static final int ANY = 0;
    /**
     * Match FluidStack fluids.
     */
    public static final int FLUID = 1;
    /**
     * Match FluidStacks data components.
     */
    public static final int DATA = 2;
    /**
     * Match FluidStacks amounts.
     */
    public static final int AMOUNT = 4;
    /**
     * Convenience value matching FluidStacks exactly by fluid, data component and amount.
     */
    public static final int EXACT = FLUID | DATA | AMOUNT;

    public static boolean areFluidStacksEqual(FluidStack a, FluidStack b, int matchFlags) {
        if (matchFlags == ANY) {
            return true;
        }
        boolean fluid  = (matchFlags & FLUID ) > 0;
        boolean nbt    = (matchFlags & DATA) > 0;
        boolean amount = (matchFlags & AMOUNT) > 0;
        return a == b || a.isEmpty() && b.isEmpty() ||
                (!a.isEmpty() && !b.isEmpty()
                        && (!fluid || a.getFluid() == b.getFluid())
                        && (!amount || a.getAmount() == b.getAmount())
                        && (!nbt || FluidStack.isSameFluidSameComponents(a, b)));
    }

}
