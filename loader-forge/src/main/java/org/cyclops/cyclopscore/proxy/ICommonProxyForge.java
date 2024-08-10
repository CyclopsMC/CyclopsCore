package org.cyclops.cyclopscore.proxy;

import org.cyclops.cyclopscore.init.ModBaseForge;


/**
 * Interface for common proxies.
 * @author rubensworks
 */
public interface ICommonProxyForge extends ICommonProxyCommon {

    /**
     * @return The mod for this proxy.
     */
    public ModBaseForge<?> getMod();

}
