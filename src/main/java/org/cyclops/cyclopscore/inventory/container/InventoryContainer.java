package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nullable;

/**
 * A container for an inventory.
 * @author rubensworks
 */
public abstract class InventoryContainer extends ContainerExtended {

	protected final IInventory inventory;

	public InventoryContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory, IInventory inventory) {
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

	public IInventory getContainerInventory() {
		return inventory;
	}

	@Override
	protected int getSizeInventory() {
		return inventory.getContainerSize();
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return this.inventory.stillValid(player);
	}

	@Override
	public void removed(PlayerEntity player) {
		super.removed(player);
		this.inventory.stopOpen(player);
	}
}
