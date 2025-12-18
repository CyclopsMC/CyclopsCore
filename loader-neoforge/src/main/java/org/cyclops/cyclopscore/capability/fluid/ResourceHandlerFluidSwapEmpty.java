package org.cyclops.cyclopscore.capability.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.BucketResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.Objects;

/**
 * A fluid resource handler that swaps an item to empty when drained.
 * Inspired by {@link BucketResourceHandler}.
 * @author rubensworks
 */
public class ResourceHandlerFluidSwapEmpty extends ItemAccessResourceHandler<FluidResource> {

    private final FluidStack fluid;
    private final ItemResource itemResourceFull;
    private final ItemResource itemResourceEmpty;

    public ResourceHandlerFluidSwapEmpty(ItemAccess itemAccess, FluidStack fluid, ItemResource itemResourceFull, ItemResource itemResourceEmpty) {
        super(itemAccess, 1);
        this.fluid = fluid;
        this.itemResourceFull = itemResourceFull;
        this.itemResourceEmpty = itemResourceEmpty;
    }

    @Override
    protected FluidResource getResourceFrom(ItemResource accessResource, int index) {
        if (itemResourceEmpty.is(accessResource.getItem())) {
            return FluidResource.EMPTY;
        }
        return FluidResource.of(fluid);
    }

    @Override
    protected int getAmountFrom(ItemResource accessResource, int index) {
        var resource = getResourceFrom(accessResource, index);
        return resource.isEmpty() ? 0 : fluid.getAmount();
    }

    @Override
    protected ItemResource update(ItemResource accessResource, int index, FluidResource newResource, int newAmount) {
        if (newAmount == 0) {
            return itemResourceEmpty;
        } else if (newAmount != fluid.getAmount()) {
            return ItemResource.EMPTY;
        } else {
            return this.itemResourceFull;
        }
    }

    @Override
    protected int getCapacity(int index, FluidResource resource) {
        Objects.checkIndex(index, size());
        return fluid.getAmount();
    }

}
