package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

import javax.annotation.Nullable;

/**
 * A container for an inventory.
 * @author rubensworks
 */
public abstract class InventoryContainerCommon extends ContainerExtendedCommon {

    protected final Container inventory;

    public InventoryContainerCommon(@Nullable MenuType<?> type, int id, Inventory playerInventory, Container inventory) {
        super(type, id, playerInventory);
        this.inventory = inventory;
        if (isAssertInventorySize()) {
            checkContainerSize(inventory, getSizeInventory());
        }
        this.inventory.startOpen(playerInventory.player);
    }

    protected boolean isAssertInventorySize() {
        return true;
    }

    public Container getContainerInventory() {
        return inventory;
    }

    @Override
    protected int getSizeInventory() {
        return inventory.getContainerSize();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }
}
