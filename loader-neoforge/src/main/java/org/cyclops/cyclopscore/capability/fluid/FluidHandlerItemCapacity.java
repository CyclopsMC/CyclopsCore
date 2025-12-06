package org.cyclops.cyclopscore.capability.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.RegistryEntries;

/**
 * An itemfluid handler with a mutable capacity.
 * @author rubensworks
 */
public class FluidHandlerItemCapacity extends ItemAccessFluidHandler implements IFluidHandlerCapacity {

    private final Fluid fluid;
    private final int capacityDefault;

    /**
     * @param itemAccess The container item, data is stored on it directly as NBT.
     * @param capacity  The maximum capacity of this fluid tank.
     */
    public FluidHandlerItemCapacity(ItemAccess itemAccess, int capacity) {
        this(itemAccess, capacity, null);
    }

    /**
     * @param itemAccess The container item, data is stored on it directly as NBT.
     * @param capacity  The maximum capacity of this fluid tank.
     * @param fluid     The accepted fluid.
     */
    public FluidHandlerItemCapacity(ItemAccess itemAccess, int capacity, Fluid fluid) {
        super(itemAccess, RegistryEntries.COMPONENT_FLUID_CONTENT.get(), capacity);
        this.fluid = fluid;
        this.capacityDefault = capacity;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return super.isValid(index, resource) && fluid == null || fluid == resource.getFluid();
    }

    @Override
    protected ItemResource update(ItemResource accessResource, int index, FluidResource newResource, int newAmount) {
        this.capacity = getTankCapacity(0); // Force overriding protected capacity field as soon as possible.
        if (newAmount == 0) {
            // We override the implementation completely to avoid NBT saving for empty fluids
            return accessResource.without(this.component);
        }
        return super.update(accessResource, index, newResource, newAmount);
    }

    @Override
    public void setTankCapacity(int tank, int capacity) {
        if (capacity == this.capacityDefault) {
            itemAccess.exchange(itemAccess.getResource().without(RegistryEntries.COMPONENT_CAPACITY.get()), itemAccess.getAmount(), Transaction.openRoot());
        } else {
            itemAccess.exchange(itemAccess.getResource().with(RegistryEntries.COMPONENT_CAPACITY.get(), capacity), itemAccess.getAmount(), Transaction.openRoot());
        }
        this.capacity = capacity;
    }

    @Override
    public int getTankCapacity(int tank) {
        return itemAccess.getResource().getOrDefault(RegistryEntries.COMPONENT_CAPACITY, this.capacity);
    }
}
