package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.KeyMapping;

/**
 * Interface for client proxies.
 * @author rubensworks
 */
public interface IClientProxyCommon extends ICommonProxyCommon {

    public KeyMapping.Category getMainKeyCategory();

}
