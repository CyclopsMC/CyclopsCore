package org.cyclops.cyclopscore.helper;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

/**
 * @author rubensworks
 */
public class FluidHelpersFabric implements IFluidHelpersFabric {
    @Override
    public int getBucketVolume() {
        return (int) FluidConstants.BUCKET;
    }
}
