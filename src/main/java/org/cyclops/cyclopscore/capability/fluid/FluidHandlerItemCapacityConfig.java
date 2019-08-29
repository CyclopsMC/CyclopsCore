package org.cyclops.cyclopscore.capability.fluid;

import net.minecraft.item.ItemStack;
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
                "fluid_handler_capacity",
                IFluidHandlerItemCapacity.class,
                new FluidHandlerItemCapacity.Storage(),
                () -> new FluidHandlerItemCapacity(ItemStack.EMPTY, 1000)
        );
    }

}
