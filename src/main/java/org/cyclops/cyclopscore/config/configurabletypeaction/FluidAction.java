package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.fluid.Fluid;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;

/**
 * The action used for {@link FluidConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class FluidAction extends ConfigurableTypeAction<FluidConfig, Fluid> {

    @Override
    public void onRegisterModInit(FluidConfig eConfig) {
        // TODO: Implement when Forge has implemented Fluids
//        FluidRegistry.registerFluid((Fluid) eConfig.getSubInstance());
//        FluidRegistry.addBucketForFluid((Fluid) eConfig.getSubInstance());
    }

}
