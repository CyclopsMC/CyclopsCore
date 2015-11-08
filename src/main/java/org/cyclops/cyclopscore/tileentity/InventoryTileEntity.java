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
    protected Map<EnumFacing, Integer> slotSidesSize;
    
    /**
     * Make new tile with an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param stackSize The maximum stacksize each slot can have
     */
    public InventoryTileEntity(int inventorySize, String inventoryName, int stackSize) {
        inventory = new SimpleInventory(inventorySize , inventoryName, stackSize);
        slotSides = Maps.newHashMap();
        slotSidesSize = Maps.newHashMap();
        for(EnumFacing side : DirectionHelpers.DIRECTIONS) {
            // Init each side to it can theoretically hold all possible slots,
            // Integer lists are not option because Java allows to autoboxing
            // and that would be required in the getter methods below.
            int array[] = new int[inventorySize];
            for(int i = 0; i < inventorySize; i++) array[i] = -1;
            slotSides.put(side, array);
            slotSidesSize.put(side, 0); 
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
        int offset = slotSidesSize.get(side);
        int i = 0;
        for(int slot : slots) {
            currentSlots[offset + i] = slot;
            i++;
        }
        slotSidesSize.put(side, offset + i);
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
