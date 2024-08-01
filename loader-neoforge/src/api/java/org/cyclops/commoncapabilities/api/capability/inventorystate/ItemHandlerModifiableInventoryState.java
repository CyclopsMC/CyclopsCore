package org.cyclops.commoncapabilities.api.capability.inventorystate;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

/**
 * An inventory state implementation that wraps around an {@link IItemHandlerModifiable}.
 * @author rubensworks
 */
public class ItemHandlerModifiableInventoryState extends ItemHandlerInventoryState implements IItemHandlerModifiable {

    // We store this again, to avoid the need of casting later on, which would introduce some (minor) overhead.
    private final IItemHandlerModifiable itemHandlerModifiable;

    public ItemHandlerModifiableInventoryState(IItemHandlerModifiable itemHandlerModifiable) {
        super(itemHandlerModifiable);
        this.itemHandlerModifiable = itemHandlerModifiable;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        setNewHash();
        itemHandlerModifiable.setStackInSlot(slot, stack);
    }
}
