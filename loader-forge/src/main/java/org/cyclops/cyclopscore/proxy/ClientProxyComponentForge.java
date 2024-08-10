package org.cyclops.cyclopscore.proxy;

/**
 * Base proxy for the client side.
 *
 * @author rubensworks
 *
 */
public abstract class ClientProxyComponentForge extends ClientProxyComponentCommon implements ICommonProxyForge, IClientProxyForge {
    public ClientProxyComponentForge(CommonProxyComponentCommon commonProxyComponent) {
        super(commonProxyComponent);
    }
}
