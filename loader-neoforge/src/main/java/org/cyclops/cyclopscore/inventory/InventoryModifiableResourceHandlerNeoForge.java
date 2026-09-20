package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * An {@link IInventoryCommonModifiable} that is backed by a {@link ResourceHandler} of items.
 * @author rubensworks
 */
public class InventoryModifiableResourceHandlerNeoForge implements IInventoryCommonModifiable {

    private final ResourceHandler<ItemResource> resourceHandler;

    public InventoryModifiableResourceHandlerNeoForge(ResourceHandler<ItemResource> resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    @Override
    public int getSlots() {
        return this.resourceHandler.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return ItemUtil.getStack(this.resourceHandler, slot);
    }

    /**
     * The transfer API has no slot setter, so the slot is emptied and refilled in one transaction.
     * Unlike a plain setter, this respects the handler's own slot validation:
     * if the slot refuses (part of) the stack, the transaction is rolled back and the slot is left as it was,
     * rather than dropping the items.
     */
    @Override
    public void setStackInSlot(int slot, ItemStack itemStack) {
        try (Transaction transaction = Transaction.openRoot()) {
            int amount = this.resourceHandler.getAmountAsInt(slot);
            if (amount > 0) {
                this.resourceHandler.extract(slot, this.resourceHandler.getResource(slot), amount, transaction);
            }
            if (!itemStack.isEmpty() && this.resourceHandler.insert(slot, ItemResource.of(itemStack),
                    itemStack.getCount(), transaction) != itemStack.getCount()) {
                return;
            }
            transaction.commit();
        }
    }
}
