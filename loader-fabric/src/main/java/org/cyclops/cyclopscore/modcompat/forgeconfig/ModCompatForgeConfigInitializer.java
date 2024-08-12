package org.cyclops.cyclopscore.modcompat.forgeconfig;

import org.apache.commons.compress.utils.Lists;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;

/**
 * @author rubensworks
 */
public class ModCompatForgeConfigInitializer implements ICompatInitializer {
    @Override
    public void initialize(IModBase mod) {
        new ConfigHandlerFabricHandler(((ModBaseFabric<?>) mod).getConfigHandler()).initialize(Lists.newArrayList());
    }
}
