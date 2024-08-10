package org.cyclops.cyclopscore.proxy;

import org.cyclops.cyclopscore.CyclopsCoreForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxyForge extends CommonProxyComponentForge {

    @Override
    public ModBaseForge<?> getMod() {
        return CyclopsCoreForge._instance;
    }

}
