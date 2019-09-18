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
		assertInventorySize(inventory, getSizeInventory());
		this.inventory.openInventory(playerInventory.player);
	}

	public IInventory getContainerInventory() {
		return inventory;
	}

	@Override
	protected int getSizeInventory() {
		return inventory.getSizeInventory();
	}

	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return this.inventory.isUsableByPlayer(player);
	}

	@Override
	public void onContainerClosed(PlayerEntity player) {
		super.onContainerClosed(player);
		this.inventory.closeInventory(player);
	}
}
