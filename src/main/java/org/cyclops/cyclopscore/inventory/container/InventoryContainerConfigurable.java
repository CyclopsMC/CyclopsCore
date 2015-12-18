package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import org.cyclops.cyclopscore.inventory.IGuiContainerProviderConfigurable;

/**
 * A container for configurables.
 * @author rubensworks
 */
public abstract class InventoryContainerConfigurable extends ExtendedInventoryContainer {

	/**
	 * Make a new instance.
	 * @param inventory The player inventory.
	 * @param guiProvider The gui provider.
	 */
	public InventoryContainerConfigurable(InventoryPlayer inventory, IGuiContainerProviderConfigurable guiProvider) {
		super(inventory, guiProvider);
	}

	/**
	 * Get the gui provider.
	 * @return The gui provider.
	 */
	public IGuiContainerProviderConfigurable getGuiProvider() {
		return (IGuiContainerProviderConfigurable) super.getGuiProvider();
	}
	
}
