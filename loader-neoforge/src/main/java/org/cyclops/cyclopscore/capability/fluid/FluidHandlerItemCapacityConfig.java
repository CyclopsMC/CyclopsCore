package org.cyclops.cyclopscore.capability.fluid;

import net.neoforged.neoforge.capabilities.ItemCapability;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfigCommon;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for the item fluid handler with configurable capacity capability.
 * @author rubensworks
 *
 */
public class FluidHandlerItemCapacityConfig extends CapabilityConfigCommon<ItemCapability<IFluidHandlerItemCapacity, Void>, ModBase<?>> {

    /**
     * The unique instance.
     */
    public static FluidHandlerItemCapacityConfig _instance;

    /**
     * Make a new instance.
     */
    public FluidHandlerItemCapacityConfig() {
        super(
                CyclopsCore._instance,
                "fluid_handler_capacity",
                eConfig -> ItemCapability.createVoid(eConfig.getId(), IFluidHandlerItemCapacity.class)
        );
    }

}
