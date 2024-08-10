package org.cyclops.cyclopscore.proxy;

import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.network.PacketHandler;

/**
 * Base proxy for the client side.
 *
 * @author rubensworks
 *
 */
public abstract class ClientProxyComponent extends ClientProxyComponentCommon implements ICommonProxy, IClientProxy {

    public ClientProxyComponent(CommonProxyComponent commonProxyComponent) {
        super(commonProxyComponent);
    }

    @Override
    public void registerKeyBindings(IKeyRegistry keyRegistry, RegisterKeyMappingsEvent event) {
        getMod().getLoggerHelper().log(Level.TRACE, "Registered key bindings");
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {
        ((CommonProxyComponent) getCommonProxyComponent()).registerPacketHandlers(packetHandler);
        getMod().getLoggerHelper().log(Level.TRACE, "Registered packet handlers");
    }

    @Override
    public void registerEventHooks() {
        super.registerEventHooks();
        NeoForge.EVENT_BUS.register(getMod().getKeyRegistry());
    }

}
