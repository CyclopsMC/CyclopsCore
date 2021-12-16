package org.cyclops.cyclopscore.capability.fluid;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

/**
 * Config for the item fluid handler with configurable capacity capability.
 * @author rubensworks
 *
 */
public class FluidHandlerItemCapacityConfig extends CapabilityConfig<IFluidHandlerItemCapacity> {

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
                IFluidHandlerItemCapacity.class
        );
    }

}
