package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import org.cyclops.cyclopscore.inventory.IGuiContainerProvider;

/**
 * An extended container.
 * @author rubensworks
 */
public abstract class ExtendedInventoryContainer extends InventoryContainer {
	
	protected IGuiContainerProvider guiProvider;

	/**
	 * Make a new instance.
	 * @param inventory The player inventory.
	 * @param guiProvider The gui provider.
	 */
	public ExtendedInventoryContainer(InventoryPlayer inventory, IGuiContainerProvider guiProvider) {
		super(inventory);
		this.guiProvider = guiProvider;
	}

	/**
	 * Get the gui provider.
	 * @return The gui provider.
	 */
	public IGuiContainerProvider getGuiProvider() {
		return guiProvider;
	}

	@Override
	public String getGuiModId() {
		return getGuiProvider().getModGui().getModId();
	}

	@Override
	public int getGuiId() {
		return getGuiProvider().getGuiID();
	}
}
