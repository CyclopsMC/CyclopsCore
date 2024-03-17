package org.cyclops.cyclopscore;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.cyclops.commoncapabilities.api.capability.inventorystate.IInventoryState;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;

/**
 * Used capabilities for this mod.
 * @author rubensworks
 */
public class Capabilities {
    public static class Block {
        public static BlockCapability<IInventoryState, Direction> INVENTORY_STATE = BlockCapability.createSided(new ResourceLocation(Reference.MOD_ID, "inventory_state"), IInventoryState.class);
    }
    public static class Item {
        public static ItemCapability<IInventoryState, Void> INVENTORY_STATE = ItemCapability.createVoid(new ResourceLocation(Reference.MOD_ID, "inventory_state"), IInventoryState.class);
        public static ItemCapability<IFluidHandlerItemCapacity, Void> FLUID_HANDLER_CAPACITY = ItemCapability.createVoid(new ResourceLocation(Reference.MOD_ID, "fluid_handler_item_capacity"), IFluidHandlerItemCapacity.class);
    }
}
