package org.cyclops.cyclopscore.fluid;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * @author rubensworks
 */
public class FluidHandlerWrapper implements ResourceHandler<FluidResource> {

    private final ResourceHandler<FluidResource> fluidHandler;

    public FluidHandlerWrapper(ResourceHandler<FluidResource> fluidHandler) {
        this.fluidHandler = fluidHandler;
    }

    @Override
    public int size() {
        return fluidHandler.size();
    }

    @Override
    public FluidResource getResource(int i) {
        return fluidHandler.getResource(i);
    }

    @Override
    public long getAmountAsLong(int i) {
        return fluidHandler.getAmountAsLong(i);
    }

    @Override
    public int getAmountAsInt(int index) {
        return fluidHandler.getAmountAsInt(index);
    }

    @Override
    public long getCapacityAsLong(int i, FluidResource fluidResource) {
        return fluidHandler.getCapacityAsLong(i, fluidResource);
    }

    @Override
    public int getCapacityAsInt(int index, FluidResource resource) {
        return fluidHandler.getCapacityAsInt(index, resource);
    }

    @Override
    public boolean isValid(int i, FluidResource fluidResource) {
        return fluidHandler.isValid(i, fluidResource);
    }

    @Override
    public int insert(int i, FluidResource fluidResource, int i1, TransactionContext transactionContext) {
        return fluidHandler.insert(i, fluidResource, i1, transactionContext);
    }

    @Override
    public int insert(FluidResource resource, int amount, TransactionContext transaction) {
        return fluidHandler.insert(resource, amount, transaction);
    }

    @Override
    public int extract(int i, FluidResource fluidResource, int i1, TransactionContext transactionContext) {
        return fluidHandler.extract(i, fluidResource, i1, transactionContext);
    }

    @Override
    public int extract(FluidResource resource, int amount, TransactionContext transaction) {
        return fluidHandler.extract(resource, amount, transaction);
    }
}
