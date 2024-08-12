package org.cyclops.cyclopscore.proxy;

import org.cyclops.cyclopscore.CyclopsCoreFabric;
import org.cyclops.cyclopscore.init.ModBaseFabric;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxyFabric extends CommonProxyComponentFabric {

    @Override
    public ModBaseFabric<?> getMod() {
        return CyclopsCoreFabric._instance;
    }

}
