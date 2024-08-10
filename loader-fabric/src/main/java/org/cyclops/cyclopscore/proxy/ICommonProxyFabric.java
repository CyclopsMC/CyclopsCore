package org.cyclops.cyclopscore.proxy;

import org.cyclops.cyclopscore.init.ModBaseFabric;


/**
 * Interface for common proxies.
 * @author rubensworks
 */
public interface ICommonProxyFabric extends ICommonProxyCommon {

    /**
     * @return The mod for this proxy.
     */
    public ModBaseFabric<?> getMod();

}
