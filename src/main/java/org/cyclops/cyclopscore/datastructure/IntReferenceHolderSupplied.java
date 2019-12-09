package org.cyclops.cyclopscore.datastructure;

import net.minecraft.util.IntReferenceHolder;

import java.util.function.Supplier;

/**
 * A read-only {@link IntReferenceHolder} that refers to a certain supplied value.
 * @author rubensworks
 */
public class IntReferenceHolderSupplied extends IntReferenceHolder {

    private final Supplier<Integer> supplier;

    public IntReferenceHolderSupplied(Supplier<Integer> supplier) {
        this.supplier = supplier;
    }

    @Override
    public int get() {
        return this.supplier.get();
    }

    @Override
    public void set(int value) {
        throw new UnsupportedOperationException("Can not set the value of a IntReferenceHolderSupplied");
    }
}
