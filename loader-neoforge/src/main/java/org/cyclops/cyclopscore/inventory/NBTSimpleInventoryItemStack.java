package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.RegistryEntries;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * A simple inventory for an ItemStack that can be stored in NBT.
 * @author rubensworks
 *
 */
public class NBTSimpleInventoryItemStack extends SimpleInventory {

    protected final ItemAccess itemAccess;
    protected final String tagName;

    /**
     * Make a new instance.
     * @param itemAccess The item access.
     * @param size The amount of slots in the inventory.
     * @param stackLimit The stack limit for each slot.
     * @param tagName The NBT tag name to store this inventory in.
     *                This should be the same tag name that is used to call the NBT read/write methods.
     */
    public NBTSimpleInventoryItemStack(ItemAccess itemAccess, int size, int stackLimit, String tagName) {
        super(size, stackLimit);
        this.itemAccess = itemAccess;
        this.tagName = tagName;

        SimpleInventory contents = itemAccess.getResource().get(RegistryEntries.COMPONENT_INVENTORY);
        if (contents != null) {
            for (int i = 0; i < contents.getContainerSize(); i++) {
                setItem(i, contents.getItem(i));
            }
        }
    }

    @Override
    public void setChanged() {
        try (var tx = Transaction.openRoot()) {
            itemAccess.exchange(
                    itemAccess.getResource().with(RegistryEntries.COMPONENT_INVENTORY, this),
                    itemAccess.getAmount(),
                    tx
            );
            tx.commit();
        }
    }

    @Override
    public void readFromNBT(ValueInput data, String tagName) {
        IModHelpers.get().getInventoryHelpers().readFromNBT(this, data, tagName);
    }

    @Override
    public void writeToNBT(ValueOutput data, String tagName) {
        IModHelpers.get().getInventoryHelpers().writeToNBT(this, data, tagName);
    }

}
