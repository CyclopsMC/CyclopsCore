package org.cyclops.cyclopscore.capability.item;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.access.HandlerItemAccess;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;

/**
 * An item handler over an {@link AbstractContainerMenu} at a given slot.
 * @author rubensworks
 */
public class ItemStackResourceHandlerContainerSlot extends ItemStackResourceHandler {

    private final AbstractContainerMenu container;
    private final int slot;

    public ItemStackResourceHandlerContainerSlot(AbstractContainerMenu container, int slot) {
        this.container = container;
        this.slot = slot;
    }

    @Override
    protected ItemStack getStack() {
        return this.container.getSlot(this.slot).getItem();
    }

    @Override
    protected void setStack(ItemStack itemStack) {
        this.container.getSlot(this.slot).set(itemStack);
    }

    public static ItemAccess asItemAccess(AbstractContainerMenu container, int slot) {
        return new HandlerItemAccess(new ItemStackResourceHandlerContainerSlot(container, slot), 0);
    }
}
