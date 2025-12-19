package org.cyclops.cyclopscore;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;

/**
 * Used capabilities for this mod.
 * @author rubensworks
 */
public class Capabilities {
    public static class Item {
        public static ItemCapability<IFluidHandlerCapacity, ItemAccess> FLUID_HANDLER_CAPACITY = ItemCapability.create(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "fluid_handler_capacity"), IFluidHandlerCapacity.class, ItemAccess.class);
    }
}
