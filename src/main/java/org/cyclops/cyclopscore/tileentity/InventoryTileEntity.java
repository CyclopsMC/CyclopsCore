package org.cyclops.cyclopscore.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.util.EnumFacing;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;

import java.util.Collection;
import java.util.Map;

/**
 * A TileEntity with a static internal inventory.
 * @author rubensworks
 *
 */
public abstract class InventoryTileEntity extends InventoryTileEntityBase {
    
    protected SimpleInventory inventory;
    protected Map<EnumFacing, int[]> slotSides;
    
    /**
     * Make new tile with an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param stackSize The maximum stacksize each slot can have
     */
    public InventoryTileEntity(int inventorySize, String inventoryName, int stackSize) {
        inventory = new SimpleInventory(inventorySize , inventoryName, stackSize);
        slotSides = Maps.newHashMap();
        for(EnumFacing side : DirectionHelpers.DIRECTIONS) {
            slotSides.put(side, new int[0]);
        }
    }
    
    /**
     * Make new tile with an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     */
    public InventoryTileEntity(int inventorySize, String inventoryName) {
        this(inventorySize , inventoryName, 64);
    }
    
    /**
     * Add mappings to slots to a certain (normalized) side of this TileEntity.
     * @param side The side to map this slots to.
     * @param slots The numerical representations of the slots to map.
     */
    protected void addSlotsToSide(EnumFacing side, Collection<Integer> slots) {
        int[] currentSlots = slotSides.get(side);
        int[] newSlots = new int[currentSlots.length + slots.size()];
        System.arraycopy(currentSlots, 0, newSlots, 0, currentSlots.length);
        int offset = currentSlots.length;
        for(int slot : slots) {
            newSlots[offset++] = slot;
        }
        slotSides.put(side, newSlots);
    }
    
    /**
     * Get the internal inventory.
     * @return The SimpleInventory.
     */
    public SimpleInventory getInventory() {
        return inventory;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return slotSides.get(side);
    }

}
