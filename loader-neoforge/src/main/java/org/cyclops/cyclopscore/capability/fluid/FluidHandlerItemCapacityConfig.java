package org.cyclops.cyclopscore.capability.fluid;

import net.neoforged.neoforge.capabilities.ItemCapability;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * Config for the item fluid handler with configurable capacity capability.
 * @author rubensworks
 *
 */
public class FluidHandlerItemCapacityConfig extends CapabilityConfigCommon<ItemCapability<IFluidHandlerItemCapacity, Void>, ModBaseNeoForge<?>> {

    /**
     * The unique instance.
     */
    public static FluidHandlerItemCapacityConfig _instance;

    /**
     * Make a new instance.
     */
    public FluidHandlerItemCapacityConfig() {
        super(
                CyclopsCoreNeoForge._instance,
                "fluid_handler_capacity",
                eConfig -> ItemCapability.createVoid(eConfig.getId(), IFluidHandlerItemCapacity.class)
        );
    }

}
