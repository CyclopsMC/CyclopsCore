package org.cyclops.cyclopscore;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;

/**
 * Used capabilities for this mod.
 * @author rubensworks
 */
public class Capabilities {
    public static class Item {
        public static ItemCapability<IFluidHandlerItemCapacity, Void> FLUID_HANDLER_CAPACITY = ItemCapability.createVoid(new ResourceLocation(Reference.MOD_ID, "fluid_handler_item_capacity"), IFluidHandlerItemCapacity.class);
    }
}
