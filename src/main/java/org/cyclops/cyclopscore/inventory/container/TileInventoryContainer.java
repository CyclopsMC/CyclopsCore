package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import javax.annotation.Nullable;

/**
 * A container for a tile entity with inventory.
 * @author rubensworks
 * @param <T> The type of tile.
 */
public abstract class TileInventoryContainer<T extends CyclopsTileEntity> extends InventoryContainer {
    
    protected T tile;

    /**
     * Make a new TileInventoryContainer.
     * @param type The container type.
     * @param id The container id.
     * @param inventory The player inventory.
     * @param tile The TileEntity for this container.
     */
    public TileInventoryContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, T tile) {
        super(type, id, inventory);
        this.tile = tile;
    }
    
    /**
     * @return The tile entity.
     */
    public T getTile() {
    	return tile;
    }

    @Override
    protected int getSizeInventory() {
        return this.player.inventory.getSizeInventory();
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return tile.canInteractWith(player);
    }

}
