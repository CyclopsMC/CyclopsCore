package org.cyclops.cyclopscore.proxy;

import org.cyclops.cyclopscore.CyclopsCoreMainFabric;
import org.cyclops.cyclopscore.init.ModBaseFabric;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxyFabric extends CommonProxyComponentFabric {

    @Override
    public ModBaseFabric<?> getMod() {
        return CyclopsCoreMainFabric._instance;
    }

}
