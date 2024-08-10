package org.cyclops.cyclopscore.proxy;

/**
 * Base proxy for the client side.
 *
 * @author rubensworks
 *
 */
public abstract class ClientProxyComponentFabric extends ClientProxyComponentCommon implements ICommonProxyFabric, IClientProxyFabric {
    public ClientProxyComponentFabric(CommonProxyComponentCommon commonProxyComponent) {
        super(commonProxyComponent);
    }
}
