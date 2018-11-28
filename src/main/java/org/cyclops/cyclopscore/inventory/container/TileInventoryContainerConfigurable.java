package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import org.cyclops.cyclopscore.inventory.IGuiContainerProviderConfigurable;
import org.cyclops.cyclopscore.tileentity.InventoryTileEntity;

/**
 * A container for a tile entity with inventory.
 * @author rubensworks
 *
 * @param <T> The type of tile.
 */
public class TileInventoryContainerConfigurable<T extends InventoryTileEntity> extends InventoryContainerConfigurable {

    protected T tile;

    /**
     * Make a new TileInventoryContainer.
     * @param inventory The player inventory.
     * @param tile The TileEntity for this container.
     */
    public TileInventoryContainerConfigurable(InventoryPlayer inventory, T tile) {
        super(inventory, (IGuiContainerProviderConfigurable) tile.getBlock());
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tile.canInteractWith(entityPlayer);
    }
    
    /**
     * @return The tile entity.
     */
    public T getTile() {
    	return tile;
    }

	@Override
	protected int getSizeInventory() {
		return getTile().getSizeInventory();
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
