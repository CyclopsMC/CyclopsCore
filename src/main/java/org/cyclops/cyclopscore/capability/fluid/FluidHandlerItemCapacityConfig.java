package org.cyclops.cyclopscore.capability.fluid;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
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

    @CapabilityInject(IFluidHandlerItemCapacity.class)
    public static Capability<IFluidHandlerItemCapacity> CAPABILITY = null;

    /**
     * Make a new instance.
     */
    public FluidHandlerItemCapacityConfig() {
        super(
                CyclopsCore._instance,
                true,
                "fluid_handler_capacity",
                "Item fluid handler with configurable capacity",
                IFluidHandlerItemCapacity.class,
                new FluidHandlerItemCapacity.Storage(),
                FluidHandlerItemCapacity.class
        );
    }

    @Override
    public boolean isDisableable() {
        return false;
    }

}
